<template>
  <div>
 <div class="content">
      <!-- 表单区域 -->
      <h2>编辑个人信息</h2>
      <el-form ref="form" :model="form" label-width="80px" label-position="top">
        <el-form-item label="姓名">
          <el-input v-model="form.username" label-width="80px" style="width:400px"></el-input>
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="form.sex" placeholder="请选择性别">
            <el-option label="男" value="男"></el-option>
            <el-option label="女" value="女"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="头像">
          <el-input v-model="form.avatar" style="width:600px ;">></el-input>
          <el-avatar size="large" :src="form.avatar" class="avatar_img"></el-avatar>
        </el-form-item>
         <el-form-item class="form_end">
          <el-button type="primary" @click="edit">立即修改</el-button>
          <router-link to="/blogs"><el-button style="margin-left:30px">取消</el-button></router-link>
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
        password: ''
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
      this.form.password = this.$store.getters.getUser.password
      console.log(this.$store.getters.getUser)
      console.log(this.form)
    },
    edit () {
      console.log(this.form)
      this.$refs.form.validate((valid) => {
        if (valid) {
          // 获取token
          console.log(valid)
          this.$http.post('/user/edit', this.form, {
            // 要有头部信息,去操作
            headers: {
              Authorization: localStorage.getItem('token')
            }
          }).then((res) => {
            this.$message.success('恭喜你!编辑成功!')
            this.$store.getters.getUser.avatar = this.form.avatar
            this.$store.getters.getUser.sex = this.form.sex
            this.$store.getters.getUser.username = this.form.username
            // this.$forceUpdate()
            this.$router.push('/blogs')
          })
        } else {
          console.log('error submit!!')
          return false
        }
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
.avatar_img{
  position:absolute;
  top: 0;
  margin-left: 20px;
}
.form_end{
    text-align: center;
}
</style>
