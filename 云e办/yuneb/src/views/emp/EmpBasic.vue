<template>
    <div>
        <div style="display: flex;justify-content: space-between;margin-top: 20px;margin-bottom: 20px;">
            <div>
                <el-input v-model="empname" @keydown.enter.native="searchEmp" style="width: 400px;margin-right: 20px;"
                    placeholder="搜索操作员" clearable></el-input>
                <el-button type="primary" @click="search" :icon="Plus">搜索</el-button>
                <el-button type="primary" @click="showSearch=!showSearch" :icon="CaretBottom">高级搜索</el-button>
            </div>
            <div style="display: flex;">
                <el-upload style="margin-right: 12px;" action="/employee/import" :auto-upload="true"
                    :before-upload="uploadpro" :on-success="afterupload" :on-error="afterupload" :on-remove="cancelup">  
                    <template #trigger>
                        <el-button type="success"    :loading="loading" :icon="Upload">{{ uploadtitle }}</el-button>
                    </template>
                </el-upload>
                <el-button type="primary" @click="DownloadFile" :icon="Download">文件下载</el-button>
                <el-button type="primary" @click="search" :icon="Plus">添加员工</el-button>
            </div>
        </div>
        <div   style="border: solid  1px blue;"  v-show="showSearch" >
                高级搜索功能暂未开发
        </div>
        <div>
            <el-table :data="emp" stripe style="width: 100%;height: 570px;">
                <el-table-column fixed type="index" label="序号" width="50" />
                <el-table-column fixed prop="name" label="姓名" width="70" />
                <el-table-column prop="gender" label="性别" width="50" />
                <el-table-column prop="workID" label="工号" width="100" />
                <el-table-column prop="birthday" label="出生日期" width="140" />
                <el-table-column prop="nation.name" label="民族" width="70" />
                <el-table-column prop="phone" label="电话号" width="150" />
                <el-table-column prop="idCard" label="身份证号" width="180" />
                <el-table-column prop="wedlock" label="婚姻状况" width="70" />
                <el-table-column prop="politicsStatus.name" label="政治面貌" width="120" />
                <el-table-column prop="email" label="电子邮箱" width="185" />
                <el-table-column prop="address" label="地址" width="200" />
                <el-table-column prop="department.name" label="所属部门" width="80" />
                <el-table-column prop="position.name" label="职位" width="80" />
                <el-table-column prop="joblevel.name" label="职位等级" width="80" />
                <el-table-column label="合同期限" width="80">
                    <template #default="{ row }">
                        <el-tag>{{ row.contractTerm }}</el-tag>年
                    </template>
                </el-table-column>
                <el-table-column fixed="right" label="操作" width="220px">
                    <template #default="{ row }">
                        <el-button type="primary" size="small" @click="dialogVisible = true">编辑</el-button>
                        <el-button type="success" size="small">查看资料</el-button>
                        <el-button type="danger" size="small" @click="deleteEmp(row.id)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div style="margin-top: 10px;display: flex;justify-content: center;">
                <el-pagination v-model:current-page="currentPage" v-model:page-size="pagesize2"
                    :page-sizes="[15, 20, 25, 30]" :small="small" :disabled="disabled" :background="background"
                    layout="sizes, prev, pager, next,jumper,total" :total="total" @size-change="handleSizeChange"
                    @current-change="handleCurrentChange" />
            </div>
        </div>

        <div>
            <el-dialog v-model="dialogVisible" title="编辑资料" width="500">
                <el-form :model="emp" label-width="80px">
                    <el-form-item label="姓名">
                        <el-input v-model="emp.name"></el-input>
                    </el-form-item>
                    <el-form-item label="性别">
                        <el-input v-model="emp.name"></el-input>
                    </el-form-item>
                </el-form>
                <template #footer>
                    <div class="dialog-footer">
                        <el-button @click="dialogVisible = false">Cancel</el-button>
                        <el-button type="primary" @click="toUpdate">
                            Confirm
                        </el-button>
                    </div>
                </template>
            </el-dialog>
        </div>
    </div>
</template>

<script setup>
import { Plus, CaretBottom, Upload, Download, Loading } from '@element-plus/icons-vue'
import { ref, onMounted } from 'vue'
import { getRequest, postRequest, deleteRequest, putRequest, exportEmp } from '@/utils/api'
import { ElMessageBox, ElMessage } from 'element-plus'


const emp = ref()
const dialogVisible = ref(false)
const pagesize2 = ref(15)
const currentPage = ref(1)
const total = ref(0)
const empname = ref('')
const uploadtitle = ref('上传文件')
const uploadicon = ref("Upload")

const loading = ref(false);
const showSearch = ref(false);

const findAll = onMounted(() => {
    getRequest('/employee/?currentPage=' + currentPage.value + '&pageSize=' + pagesize2.value + '&name=' + empname.value).then((res) => {
        emp.value = res.data.data;
        total.value = res.data.total;

    });
})

const search = () => {
    findAll();
}
//页面大小改变
const handleSizeChange = (val) => {
    pagesize2.value = val;
    findAll();
}
//当前页改变
const handleCurrentChange = (val) => {
    currentPage.value = val;
    findAll();
}
//搜索
const searchEmp = () => {
    findAll();
}
//删除

const deleteEmp = (id) => {
    ElMessageBox.confirm(
        '您确定要删除吗？',
        'Warning',
        {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
        }
    )
        .then(() => {
            deleteRequest('/employee/' + id).then((res) => {
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
    console.log(id);
}
//文件下载
const DownloadFile = () => {
    exportEmp('/employee/export', "1234").then((res) => {
        console.log(res);
    });
}
//文件上传
const uploadpro = () => {
    console.log('上传中');
    uploadtitle.value = '上传中'
    // uploadicon.value = 'Loading'
    loading.value = true;
}
const afterupload = () => {
    console.log('上传完成');
    uploadtitle.value = '上传文件'
    // uploadicon.value = 'Upload'
    loading.value = false;
    findAll();
}
//取消上传
const cancelup = () => {
    uploadtitle.value = '上传文件'
    // uploadicon.value = 'Upload'
    loading.value = false;
}
</script>

<style></style>
