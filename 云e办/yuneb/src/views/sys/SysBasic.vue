<template>
    <el-tabs
      v-model="activeName"
      type="card"
      class="demo-tabs"
    >
      <el-tab-pane label="部门管理" name="DepMana"><DepMana></DepMana></el-tab-pane>
      <el-tab-pane label="职位管理" name="PosMana"><PosMana></PosMana></el-tab-pane>
      <el-tab-pane label="职称管理" name="JoblebelMana"><JoblebelMana></JoblebelMana></el-tab-pane>
      <el-tab-pane label="奖惩规则" name="EcMana"><EcMana></EcMana></el-tab-pane>
      <el-tab-pane label="权限组" name="PermiassMana"><PermiassMana></PermiassMana></el-tab-pane>
    </el-tabs>
  </template>
  <script  setup>
  import { ref } from 'vue'  
  import DepMana from '../../components/sys/DepMana.vue'
  import PosMana from '../../components/sys/PosMana.vue'
  import EcMana from '../../components/sys/EcMana.vue'
  import JoblebelMana from '../../components/sys/JoblebeMana.vue'
  import PermiassMana from '../../components/sys/PermiassMana.vue'



  const activeName = ref('PosMana')
  
  // const handleClick = (tab, event) => {
  //   console.log(tab, event)
  // }

  const components = {
    DepMana,
    PosMana,
    EcMana,
    JoblebelMana,
    PermiassMana
  }
  </script>
  <style>
  .demo-tabs > .el-tabs__content {
    padding: 32px;
    color: #6b778c;
    font-size: 32px;
    font-weight: 600;
  }
  </style>
  