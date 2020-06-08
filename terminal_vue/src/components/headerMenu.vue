<template>
  <el-menu
  :default-active="activeIndex2"
  class="el-menu-demo"
  mode="horizontal"
  @select="handleSelect"
  background-color="#545c64"
  text-color="#fff"
  active-text-color="#ffd04b">
  <el-menu-item index="1">服务器中心</el-menu-item>
  <el-submenu index="2">
    <template slot="title">管理</template>
    <el-menu-item index="2-1">人员管理</el-menu-item>
    <el-menu-item index="2-2">服务器管理</el-menu-item>
    <el-menu-item index="2-3">课程管理</el-menu-item>
    <el-submenu index="2-4">
      <template slot="title">权限管理</template>
      <el-menu-item index="2-4-1">菜单管理</el-menu-item>
      <el-menu-item index="2-4-2">角色管理</el-menu-item>
    </el-submenu>
  </el-submenu>
  <el-menu-item index="3" disabled>API管理（暂未开放）</el-menu-item>
  <div class="item">
    <el-dropdown>
        <el-badge is-dot>
          <el-avatar icon="el-icon-user-solid"></el-avatar>
        </el-badge>
        <p class="font">何铭浩是洛夫斯基</p>
      <el-dropdown-menu slot="dropdown">
        <div class="message">
          身份：<span>管理员</span>
        </div>
        <hr/>
        <el-dropdown-item icon='el-icon-user'>个人中心</el-dropdown-item>
        <el-dropdown-item icon='el-icon-bell'>消息
          <el-badge class="mark" :value="12" />
        </el-dropdown-item>
        <el-dropdown-item icon='el-icon-delete'>注销</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</el-menu>
</template>

<script>
export default {
    name: 'headerMenu',
    data() {
      return {
        activeIndex: '1',
        activeIndex2: '1'
      };
    },
    methods: {
      handleSelect(key, keyPath) {
        console.log(key, keyPath);
      }
    }
  }
</script>
<style scoped>
  .message {
    color: #909399;
    margin: 15% 15% 15% 15%;
    font-size: 12px;
  }
  .item {
    float:right;
    margin: 10px 10px auto 10px;
  }
  .font {
    float:right;
    margin-left: 5px;
    color: #ffffff;
  }
</style>
