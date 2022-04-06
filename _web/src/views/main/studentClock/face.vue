<template>
  <a-row>
    <a-col :md="4">
      <div></div>
    </a-col>
    <a-col :md="4">
      <div></div>
    </a-col>
    <a-col :md="4">
      <a-card style="width: 370px; height: 600px;" >
        <div style="width: 322px; height: 370px;" :style="{background: imgSrc}">
          <video id="videoCamera" :width="videoWidth" :height="videoHeight" :style="{display: display}"></video>
          <canvas style="display:none;" id="canvasCamera" :width="videoWidth" :height="videoHeight" ></canvas>
        </div>
        <a-divider></a-divider>
        <a-card-meta title="签到状态" >
          <template slot="description">
            {{ clockCourse }}
          </template>
        </a-card-meta>
      </a-card>
    </a-col>
  </a-row>
</template>
<script>
import { faceIdentifyApi } from '@/api/modular/main/lightupCourse/lightupCourseManage'
import { mapGetters } from 'vuex'
export default {
  data () {
    return {
      videoWidth: 322,
      videoHeight: 370,
      imgSrc: '',
      thisCancas: null,
      thisContext: null,
      thisVideo: null,
      display: 'inline',
      displayImg: 'none',
      clockCourse: '',
      id_interval: null
    }
  },
  computed: {
    ...mapGetters(['userInfo'])
  },
  mounted() {
    const that = this
    that.getCompetence()
    that.id_interval = setInterval(function () {
      that.setImage()
    }, 2000)
  },
  destroyed() {
    clearInterval(this.id_interval)
    console.log('setImage已被销毁')
    this.stopNavigator()
    console.log('摄像头已被关闭')
  },
  methods: {
// 调用权限（打开摄像头功能）
    getCompetence () {
      var _this = this
      this.thisCancas = document.getElementById('canvasCamera')
      this.thisContext = this.thisCancas.getContext('2d')
      this.thisVideo = document.getElementById('videoCamera')
      // 旧版本浏览器可能根本不支持mediaDevices，我们首先设置一个空对象
      if (navigator.mediaDevices === undefined) {
        navigator.mediaDevices = {}
      }
      // 一些浏览器实现了部分mediaDevices，我们不能只分配一个对象
      // 使用getUserMedia，因为它会覆盖现有的属性。
      // 这里，如果缺少getUserMedia属性，就添加它。
      if (navigator.mediaDevices.getUserMedia === undefined) {
        navigator.mediaDevices.getUserMedia = function (constraints) {
          // 首先获取现存的getUserMedia(如果存在)
          var getUserMedia = navigator.webkitGetUserMedia || navigator.mozGetUserMedia || navigator.getUserMedia
          // 有些浏览器不支持，会返回错误信息
          // 保持接口一致
          if (!getUserMedia) {
            return Promise.reject(new Error('getUserMedia is not implemented in this browser'))
          }
          // 否则，使用Promise将调用包装到旧的navigator.getUserMedia
          return new Promise(function (resolve, reject) {
            getUserMedia.call(navigator, constraints, resolve, reject)
          })
        }
      }
      var constraints = { audio: false, video: { width: this.videoWidth, height: this.videoHeight, transform: 'scaleX(-1)' } }
      navigator.mediaDevices.getUserMedia(constraints).then(function (stream) {
        // 旧的浏览器可能没有srcObject
        if ('srcObject' in _this.thisVideo) {
          _this.thisVideo.srcObject = stream
        } else {
          // 避免在新的浏览器中使用它，因为它正在被弃用。
          _this.thisVideo.src = window.URL.createObjectURL(stream)
        }
        // 播放
        _this.thisVideo.onloadedmetadata = function (e) {
          _this.thisVideo.play()
        }
      }).catch(err => {
        console.log(err)
      })
    },

//  绘制图片（拍照功能）
    setImage () {
      var _this = this
      // 点击，canvas画图
      _this.thisContext.drawImage(_this.thisVideo, 0, 0, _this.videoWidth, _this.videoHeight)
      // 获取图片base64链接
      var image = this.thisCancas.toDataURL('image/png')
      if (_this.imgSrc === '') {
        console.log('setImage正在执行')
        const arr = image.split(',')
        const picBase = arr[1]
        const parameter = {}
        parameter.picBase = picBase
        parameter.avatarId = this.userInfo.avatar
        _this.imgSrc = 'url(' + image + ')'
        faceIdentifyApi(parameter).then((res) => {
          if (res.success) {
            this.clockCourse = res.data.lightupCourse + '课 打卡成功！'
            this.$message.success(res.message)
            clearInterval(this.id_interval)
          } else {
            this.$message.error(res.message)
          }
        })
      } else {
        _this.imgSrc = ''
      }
      if (_this.display === 'inline') {
        _this.display = 'none'
      } else {
        _this.display = 'inline'
      }
      this.$emit('refreshDataList', this.imgSrc)
    },
// base64转文件
    dataURLtoFile (dataurl, filename) {
      var arr = dataurl.split(',')
      var mime = arr[0].match(/:(.*?);/)[1]
      var bstr = atob(arr[1])
      var n = bstr.length
      var u8arr = new Uint8Array(n)
      while (n--) {
        u8arr[n] = bstr.charCodeAt(n)
      }
      return new File([u8arr], filename, { type: mime })
    },

// 关闭摄像头
    stopNavigator () {
      this.thisVideo.srcObject.getTracks()[0].stop()
    }
  }

}

</script>
