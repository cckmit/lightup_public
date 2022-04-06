/*
Copyright [2020] [https://www.xiaonuo.vip]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Snowy采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：

1.请不要删除和修改根目录下的LICENSE文件。
2.请不要删除和修改Snowy源码头部的版权声明。
3.请保留源码和相关描述文件的项目出处，作者声明等。
4.分发源码时候，请注明软件出处 https://gitee.com/xiaonuobase/snowy
5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/xiaonuobase/snowy
6.若您的项目无法满足以上几点，可申请商业授权，获取Snowy商业授权许可，请在官网购买授权，地址为 https://www.xiaonuo.vip
 */
package vip.xiaonuo.modular.lightup.controller;

import vip.xiaonuo.core.annotion.BusinessLog;
import vip.xiaonuo.core.annotion.Permission;
import vip.xiaonuo.core.enums.LogAnnotionOpTypeEnum;
import vip.xiaonuo.core.pojo.response.ResponseData;
import vip.xiaonuo.core.pojo.response.SuccessResponseData;
import vip.xiaonuo.modular.lightup.param.LightupCourseParam;
import vip.xiaonuo.modular.lightup.service.LightupCourseService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.annotation.Resource;
import java.util.List;

/**
 * 课程设置表控制器
 *
 * @author leiw
 * @date 2022-03-03 13:21:35
 */
@RestController
public class LightupCourseController {

    @Resource
    private LightupCourseService lightupCourseService;

    /**
     * 查询课程设置表
     *
     * @author leiw
     * @date 2022-03-03 13:21:35
     */
    @GetMapping("/lightupCourse/page")
    @BusinessLog(title = "课程设置表_查询", opType = LogAnnotionOpTypeEnum.QUERY)
    public ResponseData page(LightupCourseParam lightupCourseParam) {
        return new SuccessResponseData(lightupCourseService.page(lightupCourseParam));
    }

    /**
     * 添加课程设置表
     *
     * @author leiw
     * @date 2022-03-03 13:21:35
     */
    @PostMapping("/lightupCourse/add")
    @BusinessLog(title = "课程设置表_增加", opType = LogAnnotionOpTypeEnum.ADD)
    public ResponseData add(@RequestBody @Validated(LightupCourseParam.add.class) LightupCourseParam lightupCourseParam) {
            lightupCourseService.add(lightupCourseParam);
        return new SuccessResponseData();
    }

    /**
     * 删除课程设置表，可批量删除
     *
     * @author leiw
     * @date 2022-03-03 13:21:35
     */
    @PostMapping("/lightupCourse/delete")
    @BusinessLog(title = "课程设置表_删除", opType = LogAnnotionOpTypeEnum.DELETE)
    public ResponseData delete(@RequestBody @Validated(LightupCourseParam.delete.class) List<LightupCourseParam> lightupCourseParamList) {
            lightupCourseService.delete(lightupCourseParamList);
        return new SuccessResponseData();
    }

    /**
     * 编辑课程设置表
     *
     * @author leiw
     * @date 2022-03-03 13:21:35
     */
    @PostMapping("/lightupCourse/edit")
    @BusinessLog(title = "课程设置表_编辑", opType = LogAnnotionOpTypeEnum.EDIT)
    public ResponseData edit(@RequestBody @Validated(LightupCourseParam.edit.class) LightupCourseParam lightupCourseParam) {
            lightupCourseService.edit(lightupCourseParam);
        return new SuccessResponseData();
    }

    /**
     * 查看课程设置表
     *
     * @author leiw
     * @date 2022-03-03 13:21:35
     */
    @GetMapping("/lightupCourse/detail")
    @BusinessLog(title = "课程设置表_查看", opType = LogAnnotionOpTypeEnum.DETAIL)
    public ResponseData detail(@Validated(LightupCourseParam.detail.class) LightupCourseParam lightupCourseParam) {
        return new SuccessResponseData(lightupCourseService.detail(lightupCourseParam));
    }

    /**
     * 课程设置表列表
     *
     * @author leiw
     * @date 2022-03-03 13:21:35
     */
    @GetMapping("/lightupCourse/list")
    @BusinessLog(title = "课程设置表_列表", opType = LogAnnotionOpTypeEnum.QUERY)
    public ResponseData list(LightupCourseParam lightupCourseParam) {
        return new SuccessResponseData(lightupCourseService.list(lightupCourseParam));
    }

    /**
     * 导出系统用户
     *
     * @author leiw
     * @date 2022-03-03 13:21:35
     */
    @GetMapping("/lightupCourse/export")
    @BusinessLog(title = "课程设置表_导出", opType = LogAnnotionOpTypeEnum.EXPORT)
    public void export(LightupCourseParam lightupCourseParam) {
        lightupCourseService.export(lightupCourseParam);
    }

    @PostMapping("/studentClock/todayCourse")
    @BusinessLog(title = "今日课程", opType = LogAnnotionOpTypeEnum.QUERY)
    public ResponseData TodayCourse(LightupCourseParam lightupCourseParam) {
        return new SuccessResponseData( lightupCourseService.getCoursePageWithSortAndStatus(lightupCourseService.getTodayCoursePage(lightupCourseParam)));
    }

    @PostMapping("/studentClock/weekCourse")
    @BusinessLog(title = "本周课程", opType = LogAnnotionOpTypeEnum.QUERY)
    public ResponseData WeekCourse(LightupCourseParam lightupCourseParam) {
        return new SuccessResponseData( lightupCourseService.getCoursePageWithSortAndStatus(lightupCourseService.getWeekCoursePageWithSort(lightupCourseParam)) );
    }

}
