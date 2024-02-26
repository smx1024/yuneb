<!--
 * new page
 * @author: sx
 * @since: 2024-01-30
 * PermiassMana.vue
-->
<template>
  <div class="container">
    <div class="inputel" style="display: flex;">
      <el-input v-model="role.name" placeholder="请输入角色的英文名">
        <template #prepend>ROLE_</template>
      </el-input>
      <el-input v-model="role.nameZh" placeholder="请输入角色的中文名" />
      <el-button type="primary" @click="addRole">添加</el-button>
    </div>
    <div class="demo-collapse">
      <el-collapse v-model="activeNames" accordion v-for="(role, index) in roles" @change="handleChange">
        <el-collapse-item :title="role.nameZh" :name="role.id" :key="index">
          <el-card class="box-card">
            <template #header>
              <div class="card-header">
                <span>可访问资源</span>
                <el-button text type="primary" :icon="Delete" @click="deleteRole(role.id)" />
              </div>
            </template>
            <el-tree show-checkbox ref="tree" :data="menus" :props="defaultProps" :default-checked-keys="selectedRole"
              node-key="id" :key="index" />
            <div style="display: flex;justify-content: flex-end;">
              <el-button type="primary" size="small" @click="activeNames = -1">取消修改</el-button>
              <el-button type="primary" size="small" @click="doupdate(role.id, index)">确定修改</el-button>
            </div>
          </el-card>
        </el-collapse-item>
      </el-collapse>
    </div>
  </div>
</template>

<script setup>
import { getRequest, postRequest, putRequest, deleteRequest } from '@/utils/api';
import { ref, onMounted } from 'vue';
import { Delete } from '@element-plus/icons-vue'
import { ElMessageBox, ElMessage } from 'element-plus'

const roles = ref([]);
const menus = ref([]);
const defaultProps = {
  children: 'children',
  label: 'name'
};
const selectedRole = ref([]);
const activeNames = ref([]);
//树形组件
const tree = ref(null);
//输入框的值
const role = ref({
  name: '',
  nameZh: ''
});
//查询所有角色
const findAll = onMounted(() => {
  getRequest('/system/basic/permiss/').then((res) => {
    if (res) {
      roles.value = res;
    }
  });
})
//添加角色
const addRole = () => {
  postRequest('/system/basic/permiss/role', role.value).then((res) => {
    if (res) {
      findAll();
    }
  });
}
//删除角色
const deleteRole = (id) => {
  ElMessageBox.confirm(
    '您确定要删除这个角色吗',
    'Warning',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
    .then(() => {
      deleteRequest('/system/basic/permiss/role/' + id).then((res) => {
        if (res) {
          findAll();
        }
      });
    })
    .catch(() => {
      ElMessage({
        type: 'info',
        message: '删除取消',
      })
    })




}
const getmenus = () => {
  getRequest('/system/basic/permiss/menus').then((res) => {
    if (res) {
      menus.value = res;
    }
  });
}
//折叠触发
const handleChange = (val) => {
  getmenus();
  selectRoleById(val);
}
//根据id去查询角色的资源
const selectRoleById = (id) => {
  console.log(id);
  selectedRole.value = [];
  if (id) {
    getRequest('/system/basic/permiss/mid/' + id).then((res) => {
      if (res) {
        selectedRole.value = res;
      }
    });
  }
}
//修改角色的资源
const doupdate = (id, index) => {
  if (tree.value) {
    //获取到树形组件
    let tree1 = tree.value[index]
    //获取到选中的资源
    let ids = tree1.getCheckedKeys(true);
    putRequest('/system/basic/permiss/?rid=' + id, ids).then((res) => {
      if (res) {
        findAll();
      }
    });
  }

}
</script>

<style scoped>
.demo-collapse {
  margin-top: 20px;
  width: 500px;
}

.inputel .el-input {
  margin-right: 20px;
  width: 300px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
