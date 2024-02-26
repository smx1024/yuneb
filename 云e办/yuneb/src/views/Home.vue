<template>
  <div class="container">

    <el-container>
      <el-header class="homeHeader">
        <div class="title">sx后台管理</div>
        <div>
          <el-button :icon="Bell" type="text" @click="tochat" size="large" style="margin-right: 8px; color: black">
          </el-button>

          <el-dropdown @command="handleCommand">
            <span class="el-dropdown-link">
              {{ user.name }}<i><img :src="user.userFace"></i>
              <el-icon class="el-icon--right">
                <arrow-down />
              </el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="userinfo">个人中心</el-dropdown-item>
                <el-dropdown-item command="setting">设置</el-dropdown-item>
                <el-dropdown-item command="logout">注销登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>

        </div>
      </el-header>
      <el-container>
        <el-aside width="200px">
          <el-menu unique-opened @select="menuclick">
            <el-sub-menu :index="index + ''" v-for="(item, index) in filteredRoutes" :key="index">
              <template #title>
                <i :class="item.iconCls" style="color: #1accff;margin-right: 5px"></i>
                <span>{{ item.name }}</span>
              </template>
              <el-menu-item v-for="(children, indexj) in item.children" :key="indexj" :index="children.path">{{
                children.name }}
              </el-menu-item>
            </el-sub-menu>
          </el-menu>
        </el-aside>
        <el-main>
          <el-breadcrumb v-if="this.$route.path != '/home'" :separator-icon="ArrowRight">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>{{ this.$route.name }}</el-breadcrumb-item>
          </el-breadcrumb>
          <div v-if="this.$route.path == '/home'" class="homeWelcome">
            <div>欢迎来到sx后台管理系统</div>
            <!-- <div>当前登录用户：{{ user.name }}</div> -->
          </div>
          <router-view class="homeRouterView" />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { computed, onBeforeMount } from 'vue';

import store from '@/store';
import router from '@/router';
import { postRequest } from '@/utils/api';
import { ElMessage, ElMessageBox } from 'element-plus'
import { Bell } from '@element-plus/icons-vue'
// const user = JSON.parse(window.sessionStorage.getItem('user'))
const user = computed(() => {
  return store.state.currentAdmin;
});

const menuclick = (index) => {
  console.log(user.value);
  router.push(index);
};

const filteredRoutes = computed(() => {
  console.log(store.state.routes);
  return store.state.routes.filter((item) => !item.hidden);
  // return router.options.routes.filter((item) => !item.hidden);
});

const handleCommand = (command) => {
  if (command === 'logout') {
    ElMessageBox.confirm(
      '您要注销登录吗？',
      'Warning',
      {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning',
      }
    )
      .then(() => {
        ElMessage({
          type: 'success',
          message: '注销成功',
        });
        let res = postRequest('/logout', null);
        console.log(res);
        if (res) {
          window.sessionStorage.clear();
          //清空菜单
          store.commit('initRoutes', []);
          router.push('/');
        }
      })
      .catch(() => {
        ElMessage({
          type: 'info',
          message: '取消注销',
        })
      })


  } else if (command === 'userinfo') {
    router.push('/userinfo');
  } else if (command === 'setting') {
    router.push('/setting');
  }
}

//跳转到聊天页面
const tochat = () => {
  router.push('/home/chat');
}
</script>

<style>
.homeHeader {
  background: #409eff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 15px;
  box-sizing: border-box;
}

.homeHeader .title {
  font-size: 30px;
  font-family: 新宋体;
  color: white;
}

.homeHeader .userInfo {
  cursor: pointer;
}

.el-dropdown-link img {
  width: 48px;
  height: 48px;
  border-radius: 24px;
  margin-left: 8px;
}

.homeWelcome {
  text-align: center;
  font-size: 30px;
  font-family: 新宋体;
  color: #409eff;
  padding-top: 50px;
}

.homeRouterView {
  margin-top: 10px;
}
</style>
