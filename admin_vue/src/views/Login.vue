<template>
    <div class="login_container">
        <div class="login_box">
            <!--头像区域-->
            <div class="avatar_box">
                <img src="../assets/logo.png" alt="头像">
            </div>
            <!-- 登录表单区域 -->
            <el-form ref="loginFormRef" :model="loginForm" :rules="loginFormRules" label-width="0px" class="login_form">
                <!-- 用户名 -->
                <el-form-item prop="username">
                    <el-input v-model="loginForm.username"  prefix-icon="iconfont icon-ren"></el-input>
                </el-form-item>
                <!-- 密码 -->
                 <el-form-item prop="password">
                    <el-input type="password" v-model="loginForm.password" prefix-icon="iconfont icon-suo"></el-input>
                </el-form-item>
                <!-- 按钮 -->
                 <el-form-item class="btns">
                    <el-button type="primary" @click="login">登录</el-button>
                    <el-button type="info" @click="resetloginForm">重置</el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script>
export default {
  data () {
    return {
      loginForm: {
        username: 'admin',
        password: '111111'
      },
      loginFormRules: {
        // 验证用户名是否合法
        username: [
          { required: true, message: '请输入登录名称', trigger: 'blur' },
          { min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur' }],
        // 验证密码是否合法
        password: [
          { required: true, message: '请输入登录密码', trigger: 'blur' },
          { min: 6, max: 10, message: '长度在 6 到 10 个字符', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    // 点击重置
    resetloginForm () {
      // console.log(this)
      this.$refs.loginFormRef.resetFields()
    },
    login () {
      this.$refs.loginFormRef.validate(async (valid) => {
        const _this = this
        console.log(valid)
        // eslint-disable-next-line no-useless-return
        if (!valid) return
        this.$http.post('/admin/login', this.loginForm).then((res) => {
          console.log(res.headers)
          console.log(res)
          const token = res.headers.authorization
          // const token = res.headers.authorization
          // 把数据共享
          _this.$store.commit('SET_TOKEN', token)
          _this.$store.commit('SET_USERINFO', res.data.data)
          // 获取数据
          console.log(_this.$store.getters.getUser)
          _this.$message.success('登录成功')
          _this.$router.push('/Home')
        })
      })
    }
  }

}
</script>

<style lang="less" scoped>
.login_container{
    background-color: #2b4b6b;
    height: 100%;
}
.login_box{
    width: 450px;
    height: 300px;
    background-color: white;
    border-radius: 3px;
    position:absolute;
    left: 50%;
    top: 50%;
    transform: translate(-50%,-50%);
}
.avatar_box{
    height: 130px;
    width: 130px;
    border: 1px solid #eeeeee;
    border-radius: 50%;
    padding: 10px;
    box-shadow: 0 0 10px #dddddd;
    position: absolute;
    left: 50%;
    transform: translate(-50%,-50%);
    background-color: white;
    img{
        width: 100%;
        height: 100%;
        border-radius: 50%;
        background-color: #eeeeee;
    }
}
.btns{
    display: flex;
    justify-content: flex-end;
}
.login_form{
    position: absolute;
    bottom: 0;
    width: 100%;
    padding: 0 20px;
    box-sizing: border-box;
}

</style>
