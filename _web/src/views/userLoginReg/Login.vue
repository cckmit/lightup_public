<template>
  <div class="main">
    <a-form
      id="formLogin"
      class="user-layout-login"
      ref="formLogin"
      :form="form"
      @submit="handleSubmit"
    >
      <a-alert v-if="isLoginError" type="error" showIcon style="margin-bottom: 24px;" :message="this.accountLoginErrMsg" />
      <a-form-item>
        <a-input
          size="large"
          type="text"
          placeholder="账号"
          v-decorator="[
            'account',
            { rules: [{ required: true, message: '请输入帐户名' }, { validator: handleUsernameOrEmail }], validateTrigger: 'change'}
          ]"
        >
          <a-icon slot="prefix" type="user" :style="{ color: 'rgba(0,0,0,.25)' }"/>
        </a-input>
      </a-form-item>

      <a-form-item>
        <a-input
          size="large"
          type="password"
          autocomplete="false"
          placeholder="密码"
          v-decorator="[
            'password',
            { rules: [{ required: true, message: '请输入密码' }], validateTrigger: 'blur'}
          ]"
        >
          <a-icon slot="prefix" type="lock" :style="{ color: 'rgba(0,0,0,.25)' }"/>
        </a-input>
      </a-form-item>

      <a-form-item>
        <Verify
          @success="verifySuccess"
          :mode="'pop'"
          :captchaType="captchaType"
          :imgSize="{ width: '330px', height: '155px' }"
          ref="verify"
        ></Verify>
      </a-form-item>

      <a-form-item style="margin-top:24px">
        <a-button
          size="large"
          type="primary"
          htmlType="submit"
          class="login-button"
          :loading="state.loginBtn"
          :disabled="state.loginBtn"
        >确定</a-button>
      </a-form-item>
    </a-form>

    <two-step-captcha
      v-if="requiredTwoStepCaptcha"
      :visible="stepCaptchaVisible"
      @success="stepCaptchaSuccess"
      @cancel="stepCaptchaCancel"
    ></two-step-captcha>
  </div>
</template>

<script>
import TwoStepCaptcha from '@/components/tools/TwoStepCaptcha'
import { mapActions } from 'vuex'
import { getCaptchaOpen } from '@/api/modular/system/loginManage'
import Verify from '@/components/verifition/Verify'

export default {
  components: {
    TwoStepCaptcha,
    Verify
  },
  data () {
    var captchaTypeValue = 'clickWord'
    var min = 0
    var max = 100
    var random = Math.floor(Math.random() * (max - min)) + min

    if (random % 2 === 0) {
      captchaTypeValue = 'blockPuzzle'
    }
    if (random % 2 === 1) {
      captchaTypeValue = 'clickWord'
    }
    return {
      customActiveKey: 'tab1',
      loginBtn: false,
      // login type: 0 email, 1 username, 2 telephone
      loginType: 0,
      isLoginError: false,
      requiredTwoStepCaptcha: false,
      stepCaptchaVisible: false,
      form: this.$form.createForm(this),
      state: {
        time: 60,
        loginBtn: false,
        // login type: 0 email, 1 username, 2 telephone
        loginType: 0,
        smsSendBtn: false
      },
      accountLoginErrMsg: '',
      tenantOpen: false,
      captchaOpen: false, // 是否开启验证码
      tenantsList: [],
      loginParams: [], // 登录参数
      captchaType: captchaTypeValue
    }
  },
  created () {
    this.getCaptchaOpen()
  },
  methods: {
    ...mapActions(['Login', 'Logout', 'dictTypeData']),
    /**
     * 获取验证码开关
     */
    getCaptchaOpen () {
      getCaptchaOpen().then((res) => {
        if (res.success) {
          this.captchaOpen = res.data
        }
      })
    },
    // handler
    handleUsernameOrEmail (rule, value, callback) {
      const { state } = this
      const regex = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/
      if (regex.test(value)) {
        state.loginType = 0
      } else {
        state.loginType = 1
      }
      callback()
    },
    handleTabClick (key) {
      this.isLoginError = false
      this.customActiveKey = key
      // this.form.resetFields()
    },
    handleSubmit (e) {
      e.preventDefault()
      const {
        form: { validateFields },
        state,
        customActiveKey,
        Login
      } = this

      state.loginBtn = true
      const validateFieldsKey = customActiveKey === 'tab1' ? ['account', 'password'] : ['mobile', 'captcha']
      if (this.tenantOpen) {
        validateFieldsKey.push('tenantCode')
      }
      validateFields(validateFieldsKey, { force: true }, (err, values) => {
        this.loginParams = values
        if (!err) {
          // 是否开启验证码
          if (this.captchaOpen) {
            this.$refs.verify.show()
            state.loginBtn = false
            return
          }
          const loginParams = { ...values }
          delete loginParams.account
          loginParams.account = values.account

          if (this.tenantOpen) {
            loginParams.tenantCode = values.tenantCode
          }

          Login(loginParams)
            .then((res) => this.loginSuccess(res))
            .catch(err => this.requestFailed(err))
            .finally(() => {
              state.loginBtn = false
            })
        } else {
          setTimeout(() => {
            state.loginBtn = false
          }, 600)
        }
      })
    },
    /**
     * 获取验证码
     */
    verifySuccess(params) {
      this.loginParams.code = params.captchaVerification
      console.log(JSON.stringify(this.loginParams))
      this.Login(this.loginParams).then((res) => this.loginSuccess(res))
        .catch(err => this.requestFailed(err))
        .finally(() => {
          this.state.loginBtn = false
        })
    },
    getCaptcha (e) {
      e.preventDefault()
      const { form: { validateFields }, state } = this

      validateFields(['mobile'], { force: true }, (err, values) => {
        if (!err) {
          state.smsSendBtn = true

          const interval = window.setInterval(() => {
            if (state.time-- <= 0) {
              state.time = 60
              state.smsSendBtn = false
              window.clearInterval(interval)
            }
          }, 1000)
        }
      })
    },
    stepCaptchaSuccess () {
      this.loginSuccess()
    },
    stepCaptchaCancel () {
      this.Logout().then(() => {
        this.loginBtn = false
        this.stepCaptchaVisible = false
      })
    },
    loginSuccess (res) {
      this.$router.push({ path: '/' })
      this.isLoginError = false
      // 加载字典所有字典到缓存中
      this.dictTypeData().then((res) => { })
    },
    requestFailed (err) {
      this.accountLoginErrMsg = err
      this.isLoginError = true
    }
  }
}
</script>

<style lang="less" scoped>
.user-layout-login {
  label {
    font-size: 14px;
  }

  .getCaptcha {
    display: block;
    width: 100%;
    height: 40px;
  }

  .forge-password {
    font-size: 14px;
  }

  button.login-button {
    padding: 0 15px;
    font-size: 16px;
    height: 40px;
    width: 100%;
  }

  .user-login-other {
    text-align: left;
    margin-top: 24px;
    line-height: 22px;

    .item-icon {
      font-size: 24px;
      color: rgba(0, 0, 0, 0.2);
      margin-left: 16px;
      vertical-align: middle;
      cursor: pointer;
      transition: color 0.3s;

      &:hover {
        color: #1890ff;
      }
    }

    .register {
      float: right;
    }
  }
}
</style>
