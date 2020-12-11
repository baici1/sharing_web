<template>
  <div>
    <!-- 登录注册区域 -->
    <h1>欢迎登录</h1>
    <!-- 表单区域 -->
    <el-form
      ref="loginFormRef"
      :model="loginForm"
      :rules="loginFormRules"
      label-width="0px"
      class="login_form"
    >
      <!-- 用户名 -->
      <el-form-item prop="username">
        <el-input v-model="loginForm.username" prefix-icon="iconfont icon-ren"></el-input>
      </el-form-item>
      <!-- 密码 -->
      <el-form-item prop="password">
        <el-input type="password" v-model="loginForm.password" prefix-icon="iconfont icon-suo" show-password></el-input>
      </el-form-item>
      <!-- 按钮 -->
      <el-form-item class="btns">
        <el-button type="primary" @click="login">登录</el-button>
        <el-button type="info" @click="resetloginForm">重置</el-button>
      </el-form-item>
    </el-form>

    <div class="new">
      <p>
        <router-link to="/ForgetPw">忘记密码?</router-link>
        <router-link to="/Register">注册</router-link>
      </p>
    </div>
  </div>
</template>

<script>
export default {
  data () {
    return {
      // 表单数据
      loginForm: {
        username: 'JUYI',
        password: '111111'
      },
      loginFormRules: {
        // 验证用户名是否合法
        username: [
          { required: true, message: '请输入登录名称', trigger: 'blur' },
          {
            min: 3,
            max: 10,
            message: '长度在 3 到 10 个字符',
            trigger: 'blur'
          }
        ],
        // 验证密码是否合法
        password: [
          { required: true, message: '请输入登录密码', trigger: 'blur' },
          {
            min: 6,
            max: 10,
            message: '长度在 6 到 10 个字符',
            trigger: 'blur'
          }
        ]
      }
    }
  },
  methods: {
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
        this.$http.post('/login', this.loginForm).then((res) => {
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
          _this.$router.push('/blogs')
        })
      })
    }
  }
}
</script>

<style lang="less" scoped>
h1 {
  position: absolute;
  text-align: center;
  top: 20%;
  width: 100%;
}
.btns {
  display: flex;
  justify-content: flex-end;
}
.login_form {
  position: absolute;
  bottom: 10%;
  width: 100%;
  padding: 20px;
  box-sizing: border-box;
}
.new {
  position: absolute;
  bottom: 0;
  width: 100%;
  p {
    padding: 0 20px;
    display: flex;
    justify-content: space-between;
  }
}
</style>
