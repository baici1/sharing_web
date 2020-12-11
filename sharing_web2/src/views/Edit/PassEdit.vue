<template>
  <div>
    <div class="content">
      <!-- 表单区域 -->
      <h2>修改密码</h2>
      <el-form ref="form" :model="form" label-width="80px" label-position="top">
        <el-form-item label="姓名">
          <el-input v-model="form.username" label-width="80px" style="width:400px" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="form.sex" placeholder="请选择性别" :disabled="true">
            <el-option label="男" value="男"></el-option>
            <el-option label="女" value="女"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="头像">
          <el-input v-model="form.avatar" style="width:600px ;" :disabled="true">></el-input>
          <el-avatar size="large" :src="form.avatar" class="avatar_img"></el-avatar>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" style="width:600px ;" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" style="width:600px ;"></el-input>
        </el-form-item>
        <el-form-item prop="code">
          <el-input type="email" v-model="form.code" style="width:30%"></el-input>
          <el-button class="code" @click="getcode(form.email)" style="margin-left:30px">获取验证码</el-button>
        </el-form-item>
        <el-form-item class="form_end">
          <el-button type="primary" @click="passedit">立即修改</el-button>
          <router-link to="/blogs">
            <el-button style="margin-left:30px">取消</el-button>
          </router-link>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  data () {
    return {
      form: {
        id: '',
        username: '',
        email: '',
        sex: '',
        avatar:
          'https://cube.elemecdn.com/9/c2/f0ee8a3c7c9638a54940382568c9dpng.png',
        password: '',
        code: ''
      },
      msg: ''
    }
  },
  created () {
    this.getedit()
  },
  methods: {
    getedit () {
      this.form.id = this.$store.getters.getUser.id
      this.form.username = this.$store.getters.getUser.username
      this.form.email = this.$store.getters.getUser.email
      this.form.sex = this.$store.getters.getUser.sex
      this.form.avatar = this.$store.getters.getUser.avatar
      console.log(this.$store.getters.getUser)
    },
    passedit () {
      this.$http.post('/user/changePw?email=' + this.form.email + '&password=' + this.form.password + '&code=' + this.form.code).then(res => {
        this.$message.success('恭喜你!修改成功!')
        this.$router.push('/blogs')
      })
    },
    // 获取验证码
    getcode (email) {
      this.$http.post('/user/sendCode?email=' + email).then(res => {
        this.$message.success('恭喜你!获取验证码成功!')
      })
    }
  }
}
</script>

<style scoped>
.content {
  max-width: 960px;
  margin: 30px auto;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 25px;
}
.avatar_img {
  position: absolute;
  top: 0;
  margin-left: 20px;
}
.form_end {
  text-align: center;
}
</style>
