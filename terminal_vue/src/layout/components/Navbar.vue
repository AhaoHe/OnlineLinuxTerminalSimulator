<template>
    <div class="navbar">
        <hamburger id="hamburger-container" :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" />
        <breadcrumb id="breadcrumb-container" class="breadcrumb-container" />

        <div class="right-menu">
            <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="click">
            <div class="avatar-wrapper">
            <!-- <img :src="avatar+'?imageView2/1/w/80/h/80'" class="user-avatar"> -->
                <span style="font-size:15px;margin-right:10px" v-text="`${username}[${gname}]`"></span>
                <el-badge is-dot>
                    <el-avatar icon="el-icon-user-solid"></el-avatar>
                </el-badge>
            <i class="el-icon-caret-bottom" />
            </div>
            <el-dropdown-menu slot="dropdown">
                <el-dropdown-item v-text="role"></el-dropdown-item>
                <router-link to="/">
                    <el-dropdown-item divided>个人信息</el-dropdown-item>
                </router-link>
                <router-link to="/">
                    <el-dropdown-item>消息</el-dropdown-item>
                </router-link>
                <el-dropdown-item divided @click.native="logout">
                    <span style="display:block;">登出</span>
                </el-dropdown-item>
            </el-dropdown-menu>
        </el-dropdown>
        </div>

    </div>
</template>
<script>
import { mapGetters } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import Hamburger from '@/components/Hamburger/index'
import { Message } from 'element-ui'

export default {
    name: 'Navbar',
    components:{
        Breadcrumb,
        Hamburger,
    },
    data() {
        return {
            role: this.$store.getters.roles,
            username: this.$store.getters.username,
            gname: this.$store.getters.gname,
        }
    },
    computed: {
        ...mapGetters([
            'sidebar',
            //'avatar',
            'device'
        ])
    },
    methods: {
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    async logout() {
      await this.$store.dispatch('user/logout').then(resolve=>{
        Message({
          message: '退出成功',
          type: 'success'
        });
        this.$router.push('/login')
      }).catch(error => {
        Message.error(error||'未知错误')
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background .3s;
    -webkit-tap-highlight-color:transparent;

    &:hover {
      background: rgba(0, 0, 0, .025)
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background .3s;

        &:hover {
          background: rgba(0, 0, 0, .025)
        }
      }
    }

    .avatar-container {
      margin-right: 30px;

      .avatar-wrapper {
        margin-top: 5px;
        position: relative;

        .user-avatar {
          cursor: pointer;
          width: 40px;
          height: 40px;
          border-radius: 10px;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>
