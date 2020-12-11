<template>
  <div class="header-wraper">
    <header class="blog-header">
      <nav class="header-nav">
        <h1 class="header-title">启明星</h1>
        <el-image class="header-image" :src="require('../assets/logoctgu.png')" fit="cover"></el-image>
      </nav>
      <nav class="header-nav">

        <!-- <div class="nav-search">
          <i class="wmui icon-search"></i>
          <input type="text" maxlength="30" value>
        </div>-->
        <!-- 头像区域 -->
        <el-dropdown class="block">
         <router-link to="/blogs"><el-avatar :size="50" :src="avatar"></el-avatar></router-link>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item> <el-link :underline="false" @click="user">我的资源</el-link> </el-dropdown-item>
            <el-dropdown-item> <el-link :underline="false" @click="collect">我的收藏</el-link> </el-dropdown-item>
            <el-dropdown-item><router-link to="/Blog/add">发布资源</router-link></el-dropdown-item>
            <el-dropdown-item> <el-link :underline="false" @click="useredit">编辑个人信息</el-link> </el-dropdown-item>
            <el-dropdown-item> <el-link :underline="false" @click="passemail_edit">修改密码</el-link> </el-dropdown-item>
            <el-dropdown-item> <el-link type="warning" @click="logout" :underline="false">退出</el-link></el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </nav>
    </header>
  </div>
</template>
<script>
export default {
  name: 'Header',
  data () {
    return {
      url: '',
      avatar:
        ''
    }
  },
  methods: {
    logout () {
      const _this = this
      this.$http.get('/logout', {
        headers: {
          Authorization: localStorage.getItem('token')
        }
      }).then((res) => {
        _this.$store.commit('REMOVE_INFO')
        _this.$router.push('/login')
      })
    },
    useredit () {
      this.$router.push('/UserEdit')
    },
    passemail_edit () {
      this.$router.push('/PassEdit')
    },
    user () {
      this.$router.push('/User')
    },
    collect () {
      this.$router.push('/Collect')
    }
  },
  created () {
    if (this.$store.getters.getUser.username) {
      this.avatar = this.$store.getters.getUser.avatar
    }
  }

}
</script>
<style scoped>
.header-wraper {
  top: 0;
  display: flex;
  width: 100%;
  height: 50px;
  line-height: 50px;
  border-bottom: 1px solid #eee;
  z-index: 999;
  background-color: #ffffff;
}
.blog-header {
  display: flex;
  width: 90%;
  margin: 0 auto;
  justify-content: space-between;
  padding: 0 15px;
}
.header-title {
  margin: 0;
  line-height: 50px;
  font-size: 28px;
}
.header-nav {
  display: -webkit-box;
  margin-right: 5rem;
}
.blog-header .header-nav ul {
  list-style: none;
  margin: 0;
  line-height: 50px;
}
.blog-header .header-nav li {
  display: inline-block;
}
.blog-header .header-nav li a {
  padding: 0 15px;
}
.block {
  margin-left: 30px;
   width: 50px;
  height: 50px;
}
.header-image {
  width: 50px;
  height: 50px;
  margin-left: 30px;
}
</style>
