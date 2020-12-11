<template>
  <div class="m-container">
    <Header></Header>
    <!-- 详情页的内容 -->
    <div class="content">
      <h2>{{form.title}}</h2>
      <!-- 作者详情 -->
      <div class="userdetail">
        <img
          src="../assets/original.png"
          alt="原创"
          class="detailimg"
        />
        <span>{{user.username}}</span>
        <span>{{user.email}}</span>
        <span>时间:{{form.created}}</span>
        <el-button type="text" class="editbutton" @click="open">
          <span>
            <img
              src="../assets/tobarCollect.png"
              alt="收藏"
            />
            收藏
          </span>
        </el-button>
        <div class="edit">
          <router-link :to="{name: 'BlogEdit', params: {blogId: form.id}}">
            <el-link :underline="false" class="messageedit" v-if="ownBlog">编辑</el-link>
          </router-link>
          <el-divider direction="vertical"></el-divider>
          <el-link
            :underline="false"
            class="messageedit"
            v-if="ownBlog"
            @click="deletBlog(form.id)"
          >删除</el-link>
        </div>
        <div class="tag">
          类别:
          <el-tag size="mini">{{category}}</el-tag>
        </div>
      </div>
      <!-- <div class="markdown-body" v-html="form.content" style="margin-bottom:50px;"></div> -->
       <markdown-it-vue style="margin:20px 5px;" :content="form.content"/>
      <el-link :href='form.link'>链接:{{form.link}}</el-link>
      <p>密码:{{form.password}}</p>
    </div>
    <div class="more-toolbox">
      <ul class="toolbox-list">
        <el-badge :value="thumbsup" :hidden="this.hid">
          <el-button icon="el-icon-thumb" type="text" @click="thumbsupadd">点赞</el-button>
        </el-badge>
        <el-button icon="el-icon-s-comment" type="text" style="margin-left: 10px;">评论</el-button>
        <el-button icon="el-icon-share" type="text">分享</el-button>
        <el-button icon="el-icon-warning-outline" type="text">报错</el-button>
        <el-button icon="el-icon-more-outline" type="text"></el-button>
      </ul>
    </div>
    <!-- 评论 -->
    <div class="comment">
      <h2>评论:</h2>
      <el-divider></el-divider>
      <el-row>
      <el-avatar :src="user.avatar"></el-avatar>
        <el-col :span="8" style="float:right;">
          <el-input placeholder="请输入内容" v-model="inputcomment.contents">
            <!-- 使用clearable属性即可得到一个可清空的输入框 -->
            <el-button slot="append" icon="el-icon-plus" @click="addcomment()"></el-button>
          </el-input>
        </el-col>
        </el-row>
      <el-divider></el-divider>
      <div class="author" v-for="com in comment" :key="com.id">
        <span style="vertical-align: top;margin-left:20px;">{{user.username}}评论:</span>
        <p class="authorcom">{{com.contents}}</p>
         <el-button type="text" icon="el-icon-delete" circle style="float:right;" @click="delecomment(com.id)" v-if="delecombut===com.userId"></el-button>
        <el-divider></el-divider>
      </div>
    </div>
  </div>
</template>
<script>
import MarkdownItVue from 'markdown-it-vue'
import Header from '../components/Header'
import 'github-markdown-css/github-markdown.css' // 然后添加样式markdown-body
export default {
  name: 'BlogDetail',
  components: {
    Header,
    MarkdownItVue
  },
  // 自动刷新
  inject: ['reload'], // 引入方法
  data () {
    return {
      form: {
        categoryId: null,
        id: null,
        title: '',
        description: '',
        link: '',
        password: '',
        category: '',
        content: '',
        userId: null,
        created: ''
      },
      ownBlog: '',
      user: {
        username: '',
        email: '',
        sex: '',
        avatar: ''
      },
      users: {},
      thumbsup: 0,
      hid: true,
      comment: {},
      category: '',
      inputcomment: {
        contents: ''
      },
      delecombut: this.$store.getters.getUser.id
    }
  },
  created () {
    this.getBlog()
    this.getcomment()
  },
  mounted () {
    // 哪里需要在哪里调用
  },
  methods: {
    getcategory (id) {
      this.$http.post('/category/categoryMessage?id=' + id).then((res) => {
        this.category = res.data.data.message
      })
    },
    open () {
      this.$confirm('此操作将收藏该资源, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          this.$message({
            type: 'success',
            message: '收藏成功!'
          })
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '已取消收藏'
          })
        })
    },
    getBlog () {
      const blogId = this.$route.params.blogId
      const _this = this
      console.log(1111)
      console.log(this.$route.params)
      this.$http.get('/blog/' + blogId).then((res) => {
        console.log(res)
        console.log(res.data.data)
        _this.form = res.data.data

        //  var MarkdownIt = require('markdown-it')
        // var md = new MarkdownIt()
        // var result = md.render(_this.form.content)
        // _this.form.content = result
        _this.form.userId = res.data.data.userId
        console.log(_this.form.userId)
        this.getuser(_this.form.userId)
        this.getcategory(_this.form.categoryId)
        // 判断是否是自己的文章，能否编辑
        _this.ownBlog = _this.form.userId === _this.$store.getters.getUser.id
      })
    },
    getuser (id) {
      this.$http.post('/user/userMessage?id=' + id).then((res) => {
        console.log(res)
        this.user = res.data.data
      })
    },
    deletBlog (id) {
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
          this.$message.success('删除博客成功')
          this.$router.push('/blogs')
        })
    },
    thumbsupadd () {
      this.hid = false
      this.thumbsup++
    },
    getcomment () {
      const blogId = this.$route.params.blogId
      this.$http.get('/comment/comments?id=' + blogId).then((res) => {
        console.log(res)
        this.comment = res.data.data.records
      })
    },
    addcomment () {
      const blogId = this.$route.params.blogId
      this.$http.post('/comment/add/' + blogId, this.inputcomment, {
        headers: {
          Authorization: localStorage.getItem('token')
        }
      })
        .then((res) => {
          this.$message.success('添加评论成功')
          this.getcomment()
          this.reload()
        })
    },
    delecomment (id) {
      console.log(id)
      this.$http.delete('/comment/dele/' + id, {
        headers: {
          Authorization: localStorage.getItem('token')
        }
      }).then(res => {
        console.log(res)
        this.$message.success('删除评论成功')
        // 自动刷新
        this.reload()
      })
    }

  }

}
</script>
<style lang="less" scoped>
.content {
  margin: 10px auto 0;
  max-width: 960px;
  // min-height: 600px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px 15px;
}
.comment {
  margin: 10px auto 0;
  max-width: 960px;
  padding: 20px 15px;
}
.userdetail {
  height: 65px;
  background-color: #f7f7fc;
  border-radius: 4px;
  color: #999aaa;
  position: relative;
  padding: 10px 10px;
  position: relative;
}
.detailimg {
  width: 36px;
  height: 32px;
  margin-right: 12px;
  line-height: 32px;
}
.userdetail > span {
  vertical-align: top;
  margin-left: 20px;
}
.messageedit {
  position: relative;
  color: #8fb0c9;
  padding: 0 4px;
  font-size: 12px;
}
.edit {
  position: absolute;
  top: 0;
  right: 8px;
  line-height: 32px;
}
.editbutton {
  vertical-align: top;
  display: inline-block;
  padding: 0;
  color: #8fb0c9;
  margin-left: 10px;
}
.editbutton img {
  vertical-align: middle;
  margin-top: 0;
  margin-right: 2px;
  width: 20px;
  height: 20px;
}
.tag {
  font-size: 13px;
  padding-left: 64px;
}
.input {
  display: inline-block;
  width: 500px;
  margin-left: 50px;
  vertical-align: top;
}
.authorcom {
  vertical-align: middle;
  margin: 0 50px;
  display: inline-block;
}
.more-toolbox {
  margin: 0 auto;
  max-width: 960px;
  //   height: 20px;
  display: flex;
  background-color: #f3f7fb;
  padding: 0 15px;
  border-radius: 4px;
  justify-content: space-between;
}
.toolbox-list {
  padding: 0;
}
</style>
