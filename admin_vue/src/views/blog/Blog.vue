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
          <el-input
            @clear="page(1)"
            placeholder="请输入内容"
            clearable
            v-model="str"
          >
            <!-- 使用clearable属性即可得到一个可清空的输入框 -->
            <el-button
              slot="append"
              icon="el-icon-search"
              @click="search(str)"
            ></el-button>
          </el-input>
        </el-col>
      </el-row>
      <!-- 用户列表区域 -->
      <!-- border有边线 -->
      <!-- stripe隔行变色 -->
      <el-table :data="blogs" border stripe>
        <el-table-column type="index" label="序号"></el-table-column>
        <el-table-column label="资源题目" prop="title"></el-table-column>
        <el-table-column label="资源简要" prop="description"></el-table-column>
        <el-table-column label="创建时间" prop="created"></el-table-column>
        <el-table-column label="操作" width="140px">
          <!-- eslint-disable-next-line vue/no-unused-vars -->
          <template slot-scope="scope">
            <!-- 删除按钮 -->
            <el-button
              size="mini"
              type="danger"
              icon="el-icon-delete"
              @click="deletBlog(scope.row.id)"
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
  </div>
</template>
<script>
export default {
  data () {
    return {
    // 分页需要的数据
    // 获取商品列表参数对象
    // 商品列表
      blogs: [],
      //   总数据条数
      total: 0,
      currentPage: 1,
      pagesize: 5,
      // 当是自己资源,可以进行修改.别人的则不行
      // ownBlog: this.$store.getters.getUser.id,
      str: '',
      blogsform: {},
      dialogVisible: false
    }
  },
  created () {
    this.page(1)
  },
  methods: {
    page (currentPage) {
      const _this = this
      _this.$http.get('/blogs?currentPage=' + currentPage).then((res) => {
        console.log(res)
        // 分页赋值
        _this.currentPage = res.data.data.current
        _this.blogs = res.data.data.records
        _this.total = res.data.data.total
        _this.pagesize = res.data.data.size
      })
    },
    // 获取商品列表数据
    search (str) {
      const _this = this
      _this.$http.get('/blog/search?str=' + str).then((res) => {
        console.log(res)
        // _this.currentPage = res.data.data.current
        _this.blogs = res.data.data
        _this.total = res.data.data.length
        _this.pagesize = res.data.data.length
      })
    },
    deletBlog (id) {
      this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          console.log(localStorage.getItem('token'))
          console.log(id)
          this.$http
            .delete('/blog/dele/' + id, {
              // 要有头部信息,去操作
              headers: {
                Authorization: localStorage.getItem('token')
              }
            })
            .then((res) => {
              console.log(res)
              this.$message.success('删除成功')
              this.page(1)
            })
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          })
        })
    }

    // 展示编辑用户对话框
  }
}
</script>
<style lang="less" scoped>
.pagination {
  text-align: center;
}
</style>
