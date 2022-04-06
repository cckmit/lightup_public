package vip.xiaonuo.modular.lightup.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baidu.aip.face.AipFace;
import com.baidu.aip.face.MatchRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import vip.xiaonuo.core.context.login.LoginContextHolder;
import vip.xiaonuo.core.pojo.login.SysLoginUser;
import vip.xiaonuo.modular.lightup.entity.LightupCourse;
import vip.xiaonuo.modular.lightup.mapper.LightupCourseMapper;
import vip.xiaonuo.modular.lightup.service.FaceService;
import vip.xiaonuo.sys.modular.file.service.SysFileInfoService;
import vip.xiaonuo.sys.modular.user.param.SysUserParam;
import vip.xiaonuo.sys.modular.user.result.SysUserResult;
import vip.xiaonuo.sys.modular.user.service.SysUserService;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class FaceServiceImpl implements FaceService {

    public static final String APP_ID = "25571489";
    public static final String API_KEY = "1ac3Ig14Li1qokaQ7YhTUjNI";
    public static final String SECRET_KEY = "o7bkwG4LLXSBvDiSHeBTSFTADG1pSrsr";

    @Resource
    SysUserService sysUserService;
    @Resource
    LightupCourseMapper lightupCourseMapper;
    @Resource
    SysFileInfoService sysFileInfoService;

    @Override
    public int identify(String picBase, Long avatarId) throws Exception {
        // 初始化一个AipFace
        AipFace client = new AipFace(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

         /*
         可选：设置代理服务器地址, http和socket二选一，或者均不设置
         client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
         client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理
          */

        String image2 = getTencentUrl(avatarId);

        // image1/image2也可以为url或facetoken, 相应的imageType参数需要与之对应。
        MatchRequest req1 = new MatchRequest(picBase, "BASE64");
        MatchRequest req2 = new MatchRequest(image2, "URL");
        ArrayList<MatchRequest> requests = new ArrayList<>();
        requests.add(req1);
        requests.add(req2);

        JSONObject resObj = client.match(requests);
        try {
            System.out.println(resObj.toString(2));
            double score = resObj.getJSONObject("result").getDouble("score");
            if(score > 80.0){
                return 1;
            }
            return 0;
        } catch (Exception e){
            throw new Exception("识别失败！");
        }

    }

    @Override
    public List<LightupCourse> checkTodayCourse() {
        SysUserParam user = new SysUserParam();
        SysLoginUser SysLoginUser = LoginContextHolder.me().getSysLoginUser();
        BeanUtil.copyProperties(SysLoginUser, user);
        SysUserResult sysUserResult = sysUserService.detail(user);
        String className = sysUserResult.getSysEmpInfo().getOrgName();
        QueryWrapper<LightupCourse> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("class_name", className).eq("del_stat", 0);
        List<LightupCourse> listCourse = lightupCourseMapper.selectList(queryWrapper);

        List<LightupCourse> todayCourse = new ArrayList<>();
        listCourse.forEach(f -> {
            if(f.getWeakday().equals(getNowWeekString())){
                todayCourse.add(f);
            }
        });
        //按照时间升序排列今日课程
        todayCourse.sort(Comparator.comparing(LightupCourse::getBeginTime));
        return todayCourse;
    }

    @Override
    public String getTencentUrl(Long avatarId) {
        String key = sysFileInfoService.getById(avatarId).getFileObjectName();
        return "https://tencentoss-1258650587.cos.ap-beijing.myqcloud.com/" + key;
    }

    @Override
    public Boolean checkBeginTime(LightupCourse lightupCourse) {
        return betweenTime(lightupCourse.getBeginTime()) < 600000;
    }

    @Override
    public Boolean checkEndTime(LightupCourse lightupCourse) {
        return betweenTime(lightupCourse.getEndTime()) < 600000;
    }

    @Override
    public Boolean checkMiddleTime(LightupCourse lightupCourse) {
        Long courseLength = betweenOnlyTime(lightupCourse.getBeginTime(), lightupCourse.getEndTime());
        if( checkBeginTime(lightupCourse) || checkEndTime(lightupCourse)){
            return false;
        }
        if(betweenTime(lightupCourse.getBeginTime()) < courseLength
                && betweenTime(lightupCourse.getEndTime()) < courseLength){
            return true;
        }
        return false;
    }

    private String getNowWeekString(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        String str = simpleDateFormat.format(new Date());
        String weekNum = String.valueOf(str.charAt(str.length() - 1));
        return "周" + weekNum;
    }

    @Override
    public Long betweenTime(String compareTimeToNow) {
        //需要比对的时间
        String today = DateUtil.today();
        Date dateCompare = DateUtil.parse(today + " " + compareTimeToNow);
        //当前时间
        Date dateNow = new Date();
        return DateUtil.between(dateCompare, dateNow, DateUnit.MS);
    }
    @Override
    public Long betweenTime(String compareTime, String compareDate) throws ParseException {
        //需要比对的时间
        String today = DateUtil.today();
        Date dateCompare = DateUtil.parse(today + " " + compareTime);
        //解析时间
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(compareDate);
        return DateUtil.between(dateCompare, date, DateUnit.MS);
    }

    @Override
    public Long betweenOnlyTime(String compareTimeOne, String compareTimeTwo){
        //需要比对的时间1
        String today = DateUtil.today();
        Date dateCompareOne = DateUtil.parse(today + " " + compareTimeOne);

        //需要比对的时间2
        Date dateCompareTwo = DateUtil.parse(today + " " + compareTimeTwo);

        //时间对比
        return DateUtil.between(dateCompareOne, dateCompareTwo, DateUnit.MS);
    }

}
