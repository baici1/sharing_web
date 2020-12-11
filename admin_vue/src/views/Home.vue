<template>
  <el-container class="home-container">
    <!-- 头部区域 -->
    <el-header>
      <div>
        <img
          src="../assets/logoctgu.png"
          alt="logo"
          style="width:50px ;height:50px"
        />
        <span>管理系统</span>
      </div>
      <el-button type="info" @click="logout">退出</el-button>
    </el-header>
    <!-- 页面主体区域 -->
    <el-container>
      <!-- 侧边栏 -->
      <!-- 实现侧边栏折叠和展开的功能 -->
      <el-aside :width="isCollapse ?'64px' :'200px'">
        <!-- 折叠按钮 -->
        <div class="toggle-button" @click="toggleCollapse">|||</div>
        <el-menu :default-active="activePath" router :collapse-transition="false"  :collapse="isCollapse" background-color="#333744" text-color="#fff" active-text-color="#409EFF" unique-opened>
           <!-- :unique-opened="true"->只允许展开一个菜单 -->
           <!-- :collapse-transition="false" -> 关闭动画 -->
           <!-- router -> 导航开启路由模式 -->
          <!-- 一级菜单 -->
          <el-submenu :index="item.id+''" v-for="item in menulist" :key="item.id">
            <!-- 一级菜单模板区域 -->
            <template slot="title">
              <!-- 图标 -->
              <i :class="iconsObj[item.id]"></i>
              <!-- 文本 -->
              <span>{{item.authName}}</span>
            </template>
            <!-- 二级菜单 -->
            <el-menu-item :index="'/'+subItem.path" v-for="subItem in item.children" :key="subItem.id" @click="saveNavState('/'+subItem.path)">
               <!-- 导航开启路由模式：
                将index值作为导航路由 -->
              <!-- 二级菜单的模板区域 -->
              <template slot="title">
                <!-- 图标 -->
                <i class="el-icon-menu"></i>
                <!-- 文本 -->
                <span>{{subItem.authName}}</span>
              </template>
            </el-menu-item>
          </el-submenu>
        </el-menu>
      </el-aside>
      <!-- 右侧主体区域 -->
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
export default {
  data () {
    return {
      // 左侧菜单数据
      menulist: [
        {
          id: 101,
          authName: '用户管理',
          level: '0',
          pid: 0,
          path: null,
          children: [
            {
              id: 104,
              authName: '用户列表',
              path: 'User',
              pid: 101
            }
          ]
        },
        {
          id: 102,
          authName: '博客管理',
          level: '0',
          pid: 0,
          path: null,
          children: [
            {
              id: 104,
              authName: '博客列表',
              path: 'Blog',
              pid: 101
            }
          ]
        },
        {
          id: 125,
          authName: '类别管理',
          level: '0',
          pid: 0,
          path: null,
          children: [
            {
              id: 104,
              authName: '类别列表',
              path: 'Category',
              pid: 101
            }
          ]
        }
      ],
      isCollapse: false,
      iconsObj: {
        125: 'iconfont icon-ren1',
        //  103: 'iconfont icon-heziguanligenduoshangpinkeji',
        101: 'iconfont icon-gouwudai',
        102: 'iconfont icon-wenben'
        // 145: 'iconfont icon-shuju'
      },
      // 被激活的地址
      activePath: ''
    }
  },
  created () {
    // this.getMenuList()
    // 获得activepath
    this.activePath = window.sessionStorage.getItem('activePath')
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
    // 点击按钮,折叠菜单按钮
    toggleCollapse () {
      this.isCollapse = !this.isCollapse
    },
    // 侧边栏的保存连接的激活状态
    saveNavState (activePath) {
      window.sessionStorage.setItem('activePath', activePath)
    }

  }
}
</script>

<style lang="less" scoped>
.el-header {
  background-color: #373d41;
  display: flex;
  justify-content: space-between;
  padding-left: 0;
  align-items: center;
  color: white;
  font-size: 20px;
  > div {
    display: flex;
    align-items: center;
    span {
      margin-left: 15px;
    }
  }
}
.el-aside {
  background-color: #333744;
  .el-menu{
    border-right:none;
  }
}
.el-main {
  background-color: whitesmoke;
}
.home-container {
  height: 100%;
}
.iconfont{
  margin-right: 10px;
}
.toggle-button{
  background-color: #4A5064;
  color: white;
  font-size: 10px;
  line-height: 24px;
  text-align: center;
  letter-spacing: 0.2em;
  cursor: pointer;
}
</style>
