<template>
    <div>
        <div style="margin-top:30px;">
            <el-table :data="tableData" border style="width: 100%">
                <el-table-column type="index" label="序号" width="60" />
                <el-table-column prop="name" label="姓名" width="180" />
                <el-table-column prop="workID" label="工号" width="180" />

                <el-table-column prop="phone" label="电话号码" width="180" />
                <el-table-column prop="department.name" label="所属部门" width="180" />
                <el-table-column label="工资账套"  width="180" >
                    <template #default="{ row }">
                        <el-tooltip v-if="row.salary" class="box-item" effect="dark" placement="right">
                            <template #content>
                                基本工资：{{ row.salary.basicSalary }}<br>
                                奖金：{{ row.salary.bonus }}<br>
                                午餐补助：{{ row.salary.lunchSalary }}<br>
                                交通补助：{{ row.salary.trafficSalary }}<br>
                            </template>
                            <el-tag> {{ row.salary.name }} </el-tag>
                        </el-tooltip>

                    </template>
                </el-table-column>
                <el-table-column label="操作" width="180">
                    <template #default="{ row }">
                        <el-popover placement="top-start" title="编辑工资账套" @show="showPop(row.salary)"  @hide="hide(row)" :width="200" trigger="click">
                            <div>
                                <el-select v-model="currentsalary" placeholder="请选择">
                                    <el-option v-for="item in salary" :key="item.id" :label="item.name" :value="item.id" />
                                </el-select>
                            </div>
                            <template #reference>
                                <el-button type="primary" size="small">编辑</el-button>
                            </template>
                        </el-popover>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <div style="margin-top: 10px;display: flex;justify-content: center;">
            <el-pagination v-model:current-page="currentPage2" :page-sizes="[10, 20, 30, 40]" :small="small"
                :disabled="disabled" :background="background" layout="sizes, prev, pager, next,jumper,total" :total="total"
                @size-change="handleSizeChange" @current-change="handleCurrentChange" />
        </div>
    </div>
</template>

<script  setup>
import { ref, onMounted } from 'vue';
import { getRequest, postRequest, deleteRequest, putRequest } from '@/utils/api';
import { ElMessageBox, ElMessage } from 'element-plus';

const tableData = ref([])
const currentPage2 = ref(1)
const pagesize2 = ref(10)
const total = ref(0)
const currentsalary = ref()
const salary = ref([])

const findAll = onMounted(() => {
    getRequest('/salary/sobcfg/?currentPage=' + currentPage2.value + '&size=' + pagesize2.value).then((res) => {
        if (res) {
            tableData.value = res.data.data;
            total.value = res.data.total;
        }
    });
})

//工資套账
const findAllSalary = onMounted(() => {
    getRequest('/salary/sobcfg/salaries').then((res) => {
        salary.value = res;
    });
})
// 分页
const handleSizeChange = (val) => {
    pagesize2.value = val;
    findAll();
}
// 分页
const handleCurrentChange = (val) => {
    currentPage2.value = val;
    findAll();
}
// 编辑
const showPop = (row) => {
    if(row){
        currentsalary.value = row.id;
    }else{
        currentsalary.value = null;
    }
}
// 隐藏
const hide = (data) => {
    if(currentsalary.value&&currentsalary.value!=data.salary.id){
        putRequest('/salary/sobcfg/?eid='+data.id+'&sid='+currentsalary.value).then((res) => {
            if (res) {
                findAll();
            }
        });
    }
}
</script>

<style ></style>
