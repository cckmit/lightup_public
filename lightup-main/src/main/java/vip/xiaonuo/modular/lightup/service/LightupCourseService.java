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
package vip.xiaonuo.modular.lightup.service;

import com.baomidou.mybatisplus.extension.service.IService;
import vip.xiaonuo.core.pojo.page.PageResult;
import vip.xiaonuo.modular.lightup.entity.LightupCourse;
import vip.xiaonuo.modular.lightup.entity.LightupCourseWithSort;
import vip.xiaonuo.modular.lightup.entity.LightupCourseWithStatus;
import vip.xiaonuo.modular.lightup.param.LightupCourseParam;
import java.util.List;

/**
 * 课程设置表service接口
 *
 * @author leiw
 * @date 2022-03-03 13:21:35
 */
public interface LightupCourseService extends IService<LightupCourse> {

    /**
     * 查询课程设置表
     *
     * @author leiw
     * @date 2022-03-03 13:21:35
     */
    PageResult<LightupCourse> page(LightupCourseParam lightupCourseParam);

    /**
     * 课程设置表列表
     *
     * @author leiw
     * @date 2022-03-03 13:21:35
     */
    List<LightupCourse> list(LightupCourseParam lightupCourseParam);

    /**
     * 添加课程设置表
     *
     * @author leiw
     * @date 2022-03-03 13:21:35
     */
    void add(LightupCourseParam lightupCourseParam);

    /**
     * 删除课程设置表
     *
     * @author leiw
     * @date 2022-03-03 13:21:35
     */
    void delete(List<LightupCourseParam> lightupCourseParamList);

    /**
     * 编辑课程设置表
     *
     * @author leiw
     * @date 2022-03-03 13:21:35
     */
    void edit(LightupCourseParam lightupCourseParam);

    /**
     * 查看课程设置表
     *
     * @author leiw
     * @date 2022-03-03 13:21:35
     */
     LightupCourse detail(LightupCourseParam lightupCourseParam);

    /**
     * 导出课程设置表
     *
     * @author leiw
     * @date 2022-03-03 13:21:35
     */
     void export(LightupCourseParam lightupCourseParam);

    PageResult<LightupCourse> getTodayCoursePage(LightupCourseParam lightupCourseParam);

    PageResult<LightupCourse> getWeekCoursePageWithSort(LightupCourseParam lightupCourseParam);

    PageResult<LightupCourseWithStatus> getCoursePageWithSortAndStatus(PageResult<LightupCourse> page);
}
