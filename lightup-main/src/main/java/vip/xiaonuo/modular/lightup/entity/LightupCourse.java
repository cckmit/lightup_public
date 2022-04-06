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
package vip.xiaonuo.modular.lightup.entity;

import com.baomidou.mybatisplus.annotation.*;
import vip.xiaonuo.core.pojo.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import cn.afterturn.easypoi.excel.annotation.Excel;

/**
 * 课程设置表
 *
 * @author leiw
 * @date 2022-03-03 13:21:35
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("lightup_course")
public class LightupCourse extends BaseEntity {

    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 课程名称
     */
    @Excel(name = "课程名称")
    private String courseName;

    /**
     * 授课教师id
     */
    @Excel(name = "授课教师用户")
    private String teacherAccount;

    /**
     * 授课班级id
     */
    @Excel(name = "授课班级名称")
    private String className;

    /**
     * 授课星期
     */
    @Excel(name = "授课星期")
    private String weakday;

    /**
     * 授课节数数组，每天哪几节课
     */
    @Excel(name = "授课节数数组，每天哪几节课")
    private String beginTime;

    /**
     * 
     */
    @Excel(name = "")
    private String endTime;

    /**
     * 状态（字典 0正常 1停用 2删除）
     */
    @Excel(name = "状态（字典 0正常 1停用 2删除）")
    private Integer delStat;

    /**
     * 默认打卡状态，1，允许打卡；2，禁止打卡
     */
    @Excel(name = "默认打卡状态，1，允许打卡；2，禁止打卡")
    private Integer clockInStatus;

    /**
     * 备注
     */
    @Excel(name = "备注")
    private String remark;

}
