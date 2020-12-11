<template>
  <div>
    <Header></Header>
    <div class="content">
        <h2>欢迎分享资源</h2>
      <el-form ref="editForm" status-icon :model="editForm" :rules="rules" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="editForm.title"></el-input>
        </el-form-item>
        <el-form-item label="摘要" prop="description">
          <el-input type="textarea" :autosize="{ minRows: 1, maxRows: 10}" v-model="editForm.description"  ></el-input>
        </el-form-item>

        <el-form-item label="链接" prop="link">
          <el-input  v-model="editForm.link"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input  v-model="editForm.password" style="width:200px;"></el-input>
        </el-form-item>
        <el-form-item label="类别">
          <el-select v-model="editForm.categoryId" placeholder="请选择类别">
            <el-option  :value="cate.id"  v-for="cate in category" :key="cate.id">{{cate.id}}--{{cate.message}}</el-option>
            <!-- <el-option label="vue" value="1"></el-option> -->
          </el-select>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <mavon-editor v-model="editForm.content"/>
        </el-form-item>
        <el-form-item class="form_end">
          <el-button type="primary" @click="submitForm()">立即创建</el-button>
          <el-button><router-link to="/blogs">取消</router-link></el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import Header from '../../components/Header'
export default {
  components: { Header },
  data () {
    return {
      category: {},
      // 数据
      editForm: {
        id: null,
        title: '',
        description: '',
        link: '',
        password: '',
        categoryId: null,
        content: ''
      },
      // 限制条件
      rules: {
        title: [
          { required: true, message: '请输入标题', trigger: 'blur' },
          {
            min: 2,
            max: 50,
            message: '长度在 2 到 50 个字符',
            trigger: 'blur'
          }
        ],
        description: [
          { required: true, message: '请输入摘要', trigger: 'blur' }
        ],
        link: [
          { required: true, message: '请输入链接', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ],
        content: [
          { required: true, message: '请输入内容', trigger: 'blur' }
        ]
      }
    }
  },
  created () {
    this.getBlog()
    this.getcategory()
  },
  methods: {
    submitForm () {
      const _this = this
      this.$refs.editForm.validate((valid) => {
        if (valid) {
          // 获取token
          this.$http.post('/blog/edit', this.editForm, {
            // 要有头部信息,去操作
            headers: {
              Authorization: localStorage.getItem('token')
            }
          }).then((res) => {
            // 弹窗信息
            _this.$alert('感谢你的分享!工作室会更加强大!', '提示', {
              confirmButtonText: '确定',
              callback: action => {
                _this.$router.push('/blogs')
              }
            })
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    getBlog () {
      const blogId = this.$route.params.blogId
      const _this = this
      if (blogId) {
        this.$http.get('/blog/' + blogId).then((res) => {
          const blog = res.data.data
          _this.editForm.id = blog.id
          _this.editForm.title = blog.title
          _this.editForm.description = blog.description
          _this.editForm.link = blog.link
          _this.editForm.password = blog.password
          _this.editForm.categoryId = blog.categoryId
          _this.editForm.content = blog.content
        })
      }
    },
    getcategory () {
      this.$http.get('/category/categories').then(res => {
        console.log(res)
        this.category = res.data.data.records
        console.log(this.category)
      })
    }

  }
}
</script>

<style scoped>
h2{
    text-align: center;
}
.content {
  max-width: 960px;
  margin: 30px auto;
}
.form_end{
  text-align: center;
}
</style>
