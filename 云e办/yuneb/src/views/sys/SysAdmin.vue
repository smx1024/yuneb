<template>
    <div>
        <div class="ss">
            <el-input v-model="keyword" style="width: 400px;margin-right: 20px;" placeholder="搜索操作员" clearable></el-input>
            <el-button type="primary" @click="search" :icon="Plus">搜索</el-button>
        </div>
        <div class="card">
            <el-card class="box-card" v-for="(role, index) in admin" :key="index">
                <template #header>
                    <div class="card-header">
                        <span>{{ role.name }}</span>
                        <el-button class="button" text style="float: right;color: red;" :icon="Delete" @click="deleteAdmin(role)"></el-button>
                    </div>
                </template>
                <div>
                    <div style="display: flex;justify-content: center;"> 
                        <img :src="role.userFace" alt="图片已过期" style="width: 72px;height: 72px;border-radius: 72px;"/>
                    </div>
                    <div>
                        <p>用户名：{{ role.username }}</p>
                        <p>电话：{{ role.telephone }}</p>
                        <p>地址：{{ role.address }}</p>
                        <p>角色：<el-tag v-for="r in role.roles">{{ r.nameZh }}</el-tag></p>
                    </div>

                </div>
            </el-card>

        </div>
    </div>
</template>

<script setup>
import { Plus } from '@element-plus/icons-vue'
import { ref, onMounted } from 'vue'
import { getRequest, postRequest, deleteRequest, putRequest } from '@/utils/api'
import { Delete} from '@element-plus/icons-vue'
import { ElMessageBox, ElMessage } from 'element-plus'
const admin = ref()
const keyword = ref('')
const findAll = onMounted(() => {
    getRequest('/system/admin?keywords='+keyword.value).then((res) => {
        admin.value = res;
    });
})

//搜索
const search = () => {
   findAll();
}
//删除
const deleteAdmin = (role) => {
    ElMessageBox.confirm(
    '您确定要删除【'+role.name+'】吗？',
    'Warning',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
    .then(() => {
        deleteRequest('/system/admin/'+role.id).then((res) => {
        if (res) {
            findAll();
        }
    });
    })
    .catch(() => {
      ElMessage({
        type: 'info',
        message: 'Delete canceled',
      })
    })
}
</script>

<style setup>
.ss {
    display: flex;
    margin-bottom: 20px;
}
.card {
    display: flex;
    flex-wrap: wrap;
    justify-content: space-around;
    margin-top: 10px;
    margin-bottom: 20px;
}
.box-card {
    width: 300px;
    margin-bottom: 20px;
}
</style>
