<!--
 * new page
 * @author: sx
 * @since: 2024-01-30
 * DepMana.vue
-->
<template>
  <div class="container">
    <div style="margin-bottom: 20px;">
      <el-input v-model="deptname" placeholder="请输入部门名称进行搜索" style="width: 300px;margin-right: 20px;"></el-input>
    </div>
    <div>
      <el-tree :data="department" ref="tree" :props="defaultProps" @node-click="handleNodeClick"
        :filter-node-method="filterNode"  node-key="id"  :default-expanded-keys="defaultExpandedKeys" style="width: 600px;">
        <template #default="{ node, data }">
          <span class="custom-tree-node">
            <span>{{ node.label }}</span>
            <span>
              <el-button type="primary" size="small" @click="showdialog(data,node)"> 添加 </el-button>
              <el-button type="danger" size="small" style="margin-left: 8px" @click="remove(node, data)"> 删除 </el-button>
            </span>
          </span>
        </template>
      </el-tree>
      <div>
            <el-dialog v-model="dialogFormVisible" title="添加部门" width="500">
              <el-form :model="form">
                <el-form-item label="上级部门" :label-width="formLabelWidth">
                  {{ form.pname }}
                </el-form-item>
                <el-form-item label="添加部门名称" :label-width="formLabelWidth">
                  <el-input v-model="form.name" autocomplete="off" />
                </el-form-item>
              </el-form>
              <template #footer>
                <div class="dialog-footer">
                  <el-button @click="dialogFormVisible = false">取消</el-button>
                  <el-button type="primary" @click="append(data)">
                    确定
                  </el-button>
                </div>
              </template>
            </el-dialog>
          </div>
    </div>
  </div>
</template>

<script setup >
import { ref, onMounted, watch } from 'vue';
import { deleteRequest, getRequest, postRequest, putRequest } from '@/utils/api';
import { ElDialog, ElTree } from 'element-plus'
import { ElMessage, ElMessageBox } from 'element-plus'

const deptname = ref('');
const tree = ref(null);
const department = ref({});
const defaultProps = {
  children: 'children',
  label: 'name'
};
const dialogFormVisible = ref(false);
const form = ref({
  pname: '',
  name: '',
  parentId: -1
});
//默认展开节点
const defaultExpandedKeys = ref([]);
//对话框name
const  pname = ref('');
//nodeid
const nodeid = ref('');
//检测输入框的值进行过滤
watch(deptname, (val) => {
  tree.value.filter(val);
});

const filterNode = (value, data) => {
  if (!value) return true;
  return data.name.includes(value)
};

const findAll = onMounted(() => {
  getRequest('/system/basic/department/').then((response) => {
    department.value = response;
  });
});
//点击节点
const showdialog = (data,node) => {
  console.log(node);
  console.log(data);
  nodeid.value = data.id;
  dialogFormVisible.value = true;
  form.value.parentId = data.id;
  form.value.pname = data.name;
};
//添加
const append = (data) => {
  defaultExpandedKeys.value = []; 
  postRequest('/system/basic/department/', form.value).then((response) => {
   dialogFormVisible.value = false;
    if (response) {
      form.value = {
        name: '',
        parentId: -1
      }
      findAll();
      defaultExpandedKeys.value.push(nodeid.value);
      console.log(defaultExpandedKeys.value);
      
    }
  });
};
//删除
const remove = (node, data) => {
  ElMessageBox.confirm(
    '您确定要删除【'+data.name+"】部门吗？",
    'Warning',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
    .then(() => {
      deleteRequest('/system/basic/department/' + data.id).then((response) => {
        if (response) {
          findAll();
          nodeid.value = data.parentId;
        }
      });
      ElMessage({
        type: 'success',
        message: 'Delete completed',
      })
    })
    .catch(() => {
      ElMessage({
        type: 'info',
        message: 'Delete canceled',
      })
    })
};
//

</script>

<style scoped>
.custom-tree-node {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  padding: 3px 0;
  margin: 0;
  border-bottom: 1px solid #f0f0f0;
}
</style>
