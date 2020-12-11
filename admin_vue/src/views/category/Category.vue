<template>
  <div>
    <!-- 面包屑导航 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/Home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>类别管理</el-breadcrumb-item>
      <el-breadcrumb-item>类别列表</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片视图 -->
    <el-card class="box-card">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-input placeholder="请输入内容" clearable>
            <!-- 使用clearable属性即可得到一个可清空的输入框 -->
            <el-button slot="append" icon="el-icon-search"></el-button>
          </el-input>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="dialogVisible=true">添加类别</el-button>
        </el-col>
      </el-row>
      <el-table :data="category" border stripe>
        <el-table-column type="index" label="序号"></el-table-column>
        <el-table-column label="类别" prop="message"></el-table-column>
        <el-table-column label="操作" width="140px">
          <!-- eslint-disable-next-line vue/no-unused-vars -->
          <template slot-scope="scope">
            <el-button size="mini" type="primary" icon="el-icon-edit" @click="getcategorylist(scope.row.id)"></el-button>
            <!-- 删除按钮 -->
            <el-button
              size="mini"
              type="danger"
              icon="el-icon-delete"
              @click="deletCategory(scope.row.id)"
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
        @current-change="getcategory"
        class="pagination"
      ></el-pagination>
    </el-card>
    <!-- 添加类别 -->
    <el-dialog title="添加类别" :visible.sync="dialogVisible" width="50%" @close="categoryclose">
      <el-form ref="categoryformRef" :model="categoryform" label-width="70px" >
        <el-form-item label="类别" prop="message">
          <el-input v-model="categoryform.message"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" >
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addcategory">确 定</el-button>
      </span>
    </el-dialog>

     <el-dialog title="修改类别" :visible.sync="editdialogVisible" width="50%" @close="categoryclose">
      <el-form ref="categoryformRef" :model="categoryform" label-width="70px" >
        <el-form-item label="类别" prop="message">
          <el-input v-model="categoryform.message"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" >
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addcategory">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  inject: ['reload'], // 引入方法
  data () {
    return {
      category: [],
      total: 0,
      currentPage: 1,
      pagesize: 5,
      dialogVisible: false,
      categoryform: {
        id: null,
        message: ''
      },
      editdialogVisible: false,
      str: ''
    }
  },
  created () {
    this.getcategory(1)
  },
  methods: {
    getcategorylist (id) {
      this.editdialogVisible = true
      this.$http.post('/category/categoryMessage?id=' + id).then(res => {
        this.categoryform = res.data.data
        console.log(res)
      })
    },
    getcategory (currentPage) {
      const _this = this
      this.$http
        .get('/category/categories?currentPage=' + currentPage)
        .then((res) => {
          console.log(res)
          _this.currentPage = res.data.data.current
          _this.category = res.data.data.records
          _this.total = res.data.data.total
          _this.pagesize = res.data.data.size
        })
    },
    deletCategory (id) {
      this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          console.log(id)
          this.$http.post('/category/dele/' + id).then((res) => {
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
    addcategory () {
      this.$http.post('/category/edit', this.categoryform).then(res => {
        console.log(res)
        this.reload()
      })
    },
    categoryclose () {
      this.$refs.categoryformRef.resetFields()
    }
  }
}
</script>

<style scoped>
.pagination {
  text-align: center;
}
</style>
