<!--
 * new page
 * @author: sx
 * @since: 2024-01-30
 * EcMana.vue
-->
<template>
  <div>
    <div>
      <el-input placeholder="添加职称" v-model="jm.name" :suffix-icon="Plus" style="width: 150px; margin-right: 10px;"
        @keydown.enter.native="handelAdd"></el-input>
      <el-select v-model="jm.titleLevel" class="m-2" placeholder="职称等级" style="width: 240px">
        <el-option v-for="item in options1" :key="item" :label="item" :value="item" />
      </el-select>
      <el-button type="primary" @click="handelAdd">添加</el-button>
    </div>
    <div style="margin-top: 10px;">
      <el-table :data="jmdata" border style="width: 100%" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="编号" width="120" />
        <el-table-column prop="name" label="职称名称" width="200" />
        <el-table-column prop="titleLevel" label="职称等级" width="200" />
        <el-table-column prop="createDate" label="创建日期" width="180" />
        <el-table-column prop="enabled" label="是否启用" width="150">
          <template #default="scope">
            <el-tag v-if="scope.row.enabled" type="success">已启用</el-tag>
            <el-tag v-else type="danger">未启用</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="260">
          <template #default="scope">
            <el-button size="small" type="primary" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-button type="danger" @click="handleDeleteAll" :disabled="buttondisable">批量删除</el-button>
    </div>
    <!-- 编辑对话框 -->
    <div>
      <el-dialog v-model="dialogFormVisible" title="Shipping address" width="500">
    <el-form :model="jmform">
      <el-form-item label="职称名称" :label-width="formLabelWidth">
        <el-input v-model="jmform.name" autocomplete="off" />
      </el-form-item>
      <el-form-item label="职称等级" :label-width="formLabelWidth">
        <el-input v-model="jmform.titleLevel" autocomplete="off" />
      </el-form-item>
    <el-form-item label="是否启用" :label-width="formLabelWidth">
      <el-switch v-model="jmform.enabled" />
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

<script setup>
import { ref, onMounted } from 'vue'
import { getRequest, postRequest,deleteRequest, putRequest } from '@/utils/api'
import { ElMessageBox, ElMessage, switchProps } from 'element-plus'
const options1 = [
  '正高级',
  '副高级',
  '中级',
  '初级',
  '员级'
]
const jm = ref({
  name: '',
  titleLevel: ''
});
const jmform = ref({});
const jmdata = ref([]);
const multipleSelection = ref([]);
const buttondisable = ref(true);
const dialogFormVisible = ref(false);


const findAll = onMounted(() => {
  getRequest('/system/basic/joblevel/').then((res) => {
    jmdata.value = res;
  });
})
//添加职称
const handelAdd = () => {
  postRequest('/system/basic/joblevel/',jm.value).then((res) => {
    if (res) {
      jm.value={};
      findAll();
    }
  })
}
//批量删除
const handleDeleteAll = () => {  ElMessageBox.confirm(
  '您确定要永久删除' + multipleSelection.value.length + '个职称吗',
    'Warning',
    {
      confirmButtonText: 'OK',
      cancelButtonText: 'Cancel',
      type: 'warning',
    }
  )
    .then(() => {
      let ids = multipleSelection.value.map((item) => item.id);
      console.log(ids);
      deleteRequest('/system/basic/joblevel/', ids).then((res) => {
        if (res) {
          findAll();
        }
      });
    })
    .catch(() => {
      ElMessage({
        type: 'info',
        message: '取消删除',
      })
    })
};
//单个删除
const handleDelete = (index, row) => {
  ElMessageBox.confirm(
    '您确定要永久删除' + row.name + '职称吗',
    'Warning',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
    .then(() => {
      deleteRequest('/system/basic/joblevel/' + row.id).then((res) => {
        if (res) {
          findAll();
        }
      });
    })
    .catch(() => {
      ElMessage({
        type: 'info',
        message: '取消删除',
      })
    })
};
//编辑职称
const handleEdit = (index, row) => {
  console.log(row);
  Object.assign(jmform.value,row);
  jmform.value.createDate = '';
  dialogFormVisible.value=true;
  // console.log(form.value);
};

const handleSelectionChange = (val) => {
  multipleSelection.value = val;
  if (multipleSelection.value.length > 0) {
    buttondisable.value = false;
  } else {
    buttondisable.value = true;
  }
};
//编辑职称
const doupdate = () => {
  putRequest('/system/basic/joblevel/', jmform.value).then((res) => {
    if (res) {
      // name.value = '';
      findAll();
    }
  });
  dialogFormVisible.value = false;
};
</script>

<style scoped></style>
