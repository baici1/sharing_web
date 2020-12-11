<template>
  <div>
      <!-- 登录注册区域 -->
      <h1>欢迎注册</h1>
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
        <el-form-item prop="email">
          <el-input type="email" v-model="loginForm.email" prefix-icon="iconfont icon-xinfeng"></el-input>
        </el-form-item>
        <el-form-item prop="code">
          <el-input type="email" v-model="loginForm.code" style="width:70%"></el-input>
           <el-button class="code" @click="getcode(loginForm.email)">获取验证码</el-button>
        </el-form-item>
        <!-- 按钮 -->
        <el-form-item class="btns">
          <el-button type="primary" @click="register(loginForm.code)">注册</el-button>
          <el-button type="info" @click="resetloginForm">重置</el-button>
        </el-form-item>
      </el-form>
      <div class="new">
        <p>
            <router-link to="/ForgetPw">忘记密码?</router-link>
           <router-link to="/Login">登录</router-link>
        </p>
        <div class="clear"></div>
      </div>
    </div>
</template>

<script>
export default {
  data () {
    // 自定义校验规则
    // 1.定义变量的形式定义函数,校验规则
    // 2.validator属性指定具体校验规则
    // 验证邮箱规则
    // eslint-disable-next-line no-unused-vars
    var checkEmail = (rule, value, callback) => {
      // 验证邮箱的正则表达式
      // eslint-disable-next-line no-unused-vars
      const regEmail = /^([a-zA-Z]|[0-9])+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/
      if (regEmail.test(value)) {
        return callback()
      }
      callback(new Error('请输入合法的邮箱'))
    }
    return {
      // 表单数据
      loginForm: {
        username: '',
        password: '',
        email: '',
        code: ''
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
        ],
        // 自定义校验规则
        email: [
          // 验证是否输入
          { required: true, message: '请输入邮箱', trigger: 'blur' },
          // 验证输入是否正确
          { validator: checkEmail, trigger: 'blur' }
        ],
        code: [
          { required: true, message: '请输入验证码', trigger: 'blur' },
          {
            min: 6,
            max: 7,
            message: '长度在 6 个数字',
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
    // 获取验证码
    getcode (email) {
      this.$http.post('/user/sendCode?email=' + email).then(res => {
        this.$message.success('恭喜你!获取验证码成功!')
      })
    },
    register (code) {
      this.$http.post('/user/regist?code=' + code, this.loginForm).then(res => {
        this.$message.success('恭喜你!注册成功!')
        this.$router.push('/Login')
      })
    }
  }
}
</script>

<style lang="less" scoped>
h1 {
  position: absolute;
  text-align: center;
  top: 10%;
  width: 100%;
}

.btns {
  display: flex;
  justify-content: flex-end;
}
.login_form {
  position: absolute;
  bottom: 0;
  width: 100%;
  padding: 20px;
  box-sizing: border-box;
}
.new{
  position: absolute;
  bottom: 0;
  width: 100%;
  p{
    padding: 0 20px;
    display: flex;
  justify-content: space-between;
  }
}
</style>
