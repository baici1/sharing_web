<template>
  <div>
    <Header></Header>
    <!-- 页面区域 -->
    <div class="block">
      <el-row>
        <el-col :span="8" style="float:right;">
          <el-input placeholder="请输入内容" v-model="str" @clear="page(1)" clearable>
            <!-- 使用clearable属性即可得到一个可清空的输入框 -->
            <el-button slot="append" icon="el-icon-search" @click="search(str)"></el-button>
          </el-input>
        </el-col>
      </el-row>
      <el-timeline>
        <el-timeline-item
          :timestamp="blog.created"
          placement="top"
          v-for="blog in blogs"
          :key="blog.id"
        >
          <el-card>
            <router-link :to="{name: 'BlogDetail', params: {blogId: blog.id}}">
              <h2>{{blog.title}}</h2>
              <p>摘要:{{blog.description}}</p>
              <p>链接:{{blog.link}}</p>
              <p>密码:{{blog.password}}</p>
              <!-- <el-tag>{{blog.category}}</el-tag> -->
            </router-link>
            <!-- 失效按钮 -->
            <el-tooltip :enterable="false" effect="dark" content="链接失效,请点击!" placement="top">
              <el-button
                size="mini"
                type="danger"
                icon="el-icon-position"
                class="m_button"
                @click="failure(blog)"
              ></el-button>
            </el-tooltip>
            <router-link :to="{name: 'BlogEdit', params: {blogId: blog.id}}">
                <el-button
                  v-if="blog.userId===ownBlog"
                  size="mini"
                  type="warning"
                  icon="el-icon-edit"
                  class="m_button_edit"
                ></el-button>
            </router-link>
          </el-card>
        </el-timeline-item>
      </el-timeline>
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
    </div>
  </div>
</template>

<script>
import Header from '../../components/Header'
export default {
  components: { Header },
  data () {
    return {
      blogs: {},
      bloglist: {
        id: null,
        userId: null,
        title: '',
        description: '',
        link: '',
        password: '',
        category: ''
      },
      currentPage: 1,
      total: 0,
      pagesize: 5,
      // 当是自己资源,可以进行修改.别人的则不行
      ownBlog: this.$store.getters.getUser.id,
      str: ''
    }
  },
  methods: {
    page (currentPage) {
      var j = 0
      const _this = this
      _this.$http.get('/blogs?currentPage=' + currentPage).then((res) => {
        console.log(res)
        // 分页赋值
        _this.currentPage = res.data.data.current
        // _this.blogs = res.data.data.records
        for (let index = 0; index < res.data.data.records.length; index++) {
          if (res.data.data.records[index].userId === this.ownBlog) {
            _this.blogs[j++] = res.data.data.records[index]
          }
        }
        _this.total = res.data.data.total
        _this.pagesize = res.data.data.size
        console.log(this.blogs)
      })
    },
    search (str) {
      const _this = this
      _this.$http.get('/blog/search?str=' + str).then((res) => {
        console.log(res)
        _this.currentPage = res.data.data.current
        _this.blogs = res.data.data
        _this.total = res.data.data.length
        _this.pagesize = res.data.data.length
      })
    },
    failure (blog) {
      console.log(blog)
      this.bloglist.id = blog.id
      this.bloglist.userId = blog.userId
      this.bloglist.title = blog.title
      this.bloglist.description = blog.description
      this.bloglist.link = blog.link
      this.bloglist.password = blog.password
      // this.bloglist.category = blog.category
      console.log(this.bloglist)
      this.$http.post('/blog/failure', this.bloglist).then((res) => {
        // console.log(res)
        this.$message.success('发送成功')
      })
    }
  },
  created () {
    this.page(1)
  }
}
</script>

<style lang="less" scoped>
.block {
  // margin-top: 30px;
  max-width: 960px;
  margin: 30px auto;
}
.pagination {
  margin: 0 auto;
  text-align: center;
}
.m_button {
  float: right;
}
.m_button_edit {
  float: right;
  margin-right: 10px;
}
.nav-search {
  float: right;
  width: 200px;
}
</style>
