<template>
  <div>
    <!-- 面包屑导航 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/Home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>商品管理</el-breadcrumb-item>
      <el-breadcrumb-item>商品列表</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片视图区域 -->
    <el-card class="box-card">
      <el-row>
        <el-col :span="8">
          <el-input @clear="page()" placeholder="请输入内容" clearable v-model="str">
            <!-- 使用clearable属性即可得到一个可清空的输入框 -->
            <el-button slot="append" icon="el-icon-search" @click="page()"></el-button>
          </el-input>
        </el-col>
      </el-row>
      <!-- 用户列表区域 -->
      <!-- border有边线 -->
      <!-- stripe隔行变色 -->
      <el-table :data="users" border stripe>
        <el-table-column type="index" label="序号"></el-table-column>
        <el-table-column label="用户名" prop="username"></el-table-column>
        <el-table-column label="性别" prop="sex"></el-table-column>
        <el-table-column label="邮箱" prop="email"></el-table-column>
        <el-table-column label="操作" width="140px">
          <!-- eslint-disable-next-line vue/no-unused-vars -->
          <template slot-scope="scope">
            <!-- 修改按钮 -->
            <el-button
              size="mini"
              type="primary"
              icon="el-icon-edit"
              @click="showUserDialog(scope.row.id)"
            ></el-button>
            <!-- 删除按钮 -->
            <el-button
              size="mini"
              type="danger"
              icon="el-icon-delete"
              @click="deletUser(scope.row.id)"
            ></el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页区域 -->
     <el-pagination
        background
        layout="prev, pager, next"
        :current-page="currentPage"
        :page-size="pagesize"
        :total="total"
        @current-change="page"
        class="pagination"
      ></el-pagination>
    </el-card>
    <!-- 修改用户对话框 -->
    <el-dialog title="修改用户" :visible.sync="dialogVisible" width="60%" @close="userclosed">
      <el-form ref="userformRef" :model="userform" label-width="70px"   :rules="userformrules">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="userform.username"></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-input v-model="userform.sex"></el-input>
        </el-form-item>
         <el-form-item label="邮箱" prop="email">
          <el-input v-model="userform.email"></el-input>
        </el-form-item>
         <el-form-item label="密码" prop="password">
          <el-input v-model="userform.password" disabled></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="edit">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
export default {
  inject: ['reload'], // 引入方法
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
      // 控制对话框的显示和隐藏
      dialogVisible: false,
      // 商品列表
      users: [],
      //   总数据条数
      total: 0,
      currentPage: 1,
      pagesize: 5,
      // 当是自己资源,可以进行修改.别人的则不行
      // ownBlog: this.$store.getters.getUser.id,
      str: '',
      userform: {
        id: '',
        username: '',
        email: '',
        sex: '',
        password: ''
      },
      // 验证要求
      userformrules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          {
            min: 3,
            max: 10,
            message: '长度在 3 到 10 个字符',
            trigger: 'blur'
          }
        ],
        // 验证密码是否合法
        password: [
          { required: true, message: '请MD5加密后输入', trigger: 'blur' },
          {
            min: 6,
            message: '请MD5加密后输入',
            trigger: 'blur'
          }
        ],
        email: [
          // 验证是否输入
          { required: true, message: '请输入邮箱', trigger: 'blur' },
          // 验证输入是否正确
          { validator: checkEmail, trigger: 'blur' }
        ],
        sex: [
          { required: true, message: '请输入性别', trigger: 'blur' }
        ]
      }
    }
  },
  created () {
    this.page(1)
  },
  methods: {
    page (currentPage) {
      const _this = this
      _this.$http.get('/user/users?currentPage=' + currentPage).then((res) => {
        console.log(res)
        // 分页赋值
        _this.currentPage = res.data.data.current
        _this.users = res.data.data.records
        _this.total = res.data.data.total
        _this.pagesize = res.data.data.size
      })
    },
    deletUser (id) {
      this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          console.log(localStorage.getItem('token'))
          console.log(id)
          this.$http
            .post('/user/dele/' + id, {
              // 要有头部信息,去操作
              headers: {
                Authorization: localStorage.getItem('token')
              }
            })
            .then((res) => {
              console.log(res)
              this.$message.success('删除成功')
              this.reload()
            })
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          })
        })
    },
    edit () {
      console.log(this.userform)
      this.$refs.userformRef.validate((valid) => {
        if (valid) {
          // 获取token
          console.log(valid)
          this.$http.post('/user/adminedit', this.userform).then((res) => {
            this.$message.success('恭喜你!编辑成功!')
            // this.$forceUpdate()
            this.dialogVisible = false
            this.reload()
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    // 展示编辑用户对话框
    showUserDialog (id) {
      console.log(id)
      this.$http.post('/user/message?id=' + id).then((res) => {
        if (res.data.code !== 200) {
          this.$message.error('查询失败')
        }
        console.log(res)
        this.userform = res.data.data
        console.log(this.userform)
      })
      this.dialogVisible = true
    },
    userclosed () {
      this.$refs.userformRef.resetFields()
    }
  }
}
</script>
<style lang="less" scoped>
.pagination {
  text-align: center;
}
</style>
