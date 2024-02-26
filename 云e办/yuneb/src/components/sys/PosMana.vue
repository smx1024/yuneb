<!--
 * new page
 * @author: sx
 * @since: 2024-01-30
 * EcMana.vue
-->
<template>
  <div class="container">
    <div style="margin-bottom: 20px;">
      <el-input v-model="name" placeholder="添加职位" :suffix-icon="Plus" style="width: 150px; margin-right: 10px;"
        @keydown.enter.native="handleAdd"></el-input>
      <el-button type="primary" @click="handleAdd">添加</el-button>
    </div>
    <div>
      <el-table stripe border @selection-change="handleSelectionChange" :data="positions" style="width: 100%">
        <el-table-column type="selection" width="100" />
        <el-table-column type=“index” prop="id" label="编号" width="150" />
        <el-table-column prop="name" label="职位" width="250" />
        <el-table-column prop="createDate" label="创建时间" width="250"/>
        <el-table-column label="Operations" width="350">
          <template #default="scope">
            <el-button size="small" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-button type="danger" @click="handleDeleteAll" :disabled="buttondisable">批量删除</el-button>
    </div>
    <div>

      <el-dialog v-model="dialogFormVisible" title="编辑职位" width="500">
        <el-form :model="form">
          <el-form-item label="职位名称" :label-width="formLabelWidth">
            <el-input autocomplete="off" v-model="updatedPos.name" />
          </el-form-item>
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="dialogFormVisible = false">取消</el-button>
            <el-button type="primary" @click="doupdate">
              确认
            </el-button>
          </div>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script setup >
import { ref } from 'vue';
import { Plus } from '@element-plus/icons-vue'
import { onMounted } from 'vue';
import { deleteRequest, getRequest, postRequest, putRequest } from '@/utils/api';
import { ElMessage, ElMessageBox, alertProps } from 'element-plus'

const name = ref('');
const positions = ref([]);
const dialogFormVisible = ref(false);
const updatedPos = ref({});
const multipleSelection = ref([]);
const buttondisable = ref(true);
const findAll = onMounted(() => {
  getRequest('system/basic/pos/', null).then((res) => {
    positions.value = res;
  });
});
//编辑
const handleEdit = (index, row) => {
  Object.assign(updatedPos.value, row);
  updatedPos.value.createDate = '';
  dialogFormVisible.value = true;
};

//删除
const handleDelete = (index, row) => {
  ElMessageBox.confirm(
    '您确定要永久删除' + row.name + '吗',
    'Warning',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
    .then(() => {


    })
    .catch(() => {
      ElMessage({
        type: 'info',
        message: 'Delete canceled',
      })
    })
};

//添加职位
const handleAdd = () => {
  postRequest('system/basic/pos/', { name: name.value }).then((res) => {
    if (res.success) {
      name.value = '';
      findAll();
    }
  });
};
//编辑职位
const doupdate = () => {
  putRequest('system/basic/pos/', updatedPos.value).then((res) => {
    if (res) {
      // name.value = '';
      findAll();
    }
  });
  dialogFormVisible.value = false;
};

const handleSelectionChange = (val) => {
  multipleSelection.value = val;
  if (multipleSelection.value.length > 0) {
    buttondisable.value = false;
  }else{
    buttondisable.value = true;
  }
};
//批量删除
const handleDeleteAll = () => {
  ElMessageBox.confirm(
    '您确定要永久删除' + multipleSelection.value.length + '个职位吗',
    'Warning',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
    .then(() => {
      let ids = multipleSelection.value.map((item) => item.id);
      console.log(ids);
      deleteRequest('system/basic/pos/', ids).then((res) => {
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
};

</script>

<style scoped></style>
