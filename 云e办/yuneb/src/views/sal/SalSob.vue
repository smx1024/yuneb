<template>
    <div>
        <div style="display: flex;justify-content: space-between;margin-top: 15px;margin-bottom: 10px;">
            <el-button type="primary" @click="toadd">添加工资账套</el-button>
            <el-button type="success"></el-button>
        </div>
        <div>
            <el-table :data="emp">
                <el-table-column type="selection" width="55" />
                <el-table-column prop="name" label="名称"></el-table-column>
                <el-table-column prop="basicSalary" label="基本工资"></el-table-column>
                <el-table-column prop="trafficSalary" label="交通补助"></el-table-column>
                <el-table-column prop="lunchSalary" label="午餐补助"></el-table-column>
                <el-table-column label="养老金">
                    <el-table-column prop="pensionBase" label="基数"></el-table-column>
                    <el-table-column prop="pensionPer" label="比率"></el-table-column>
                </el-table-column>
                <el-table-column label="医疗保险">
                    <el-table-column prop="medicalBase" label="基数"></el-table-column>
                    <el-table-column prop="medicalPer" label="比率"></el-table-column>
                </el-table-column>
                <el-table-column label="公积金">
                    <el-table-column prop="accumulationFundBase" label="基数"></el-table-column>
                    <el-table-column prop="accumulationFundPer" label="比率"></el-table-column>
                </el-table-column>
                <el-table-column label="操作" width="150px">
                    <template #default="{ row }">
                        <el-button type="primary" size="small" @click="edit(row)">编辑</el-button>
                        <el-button type="danger" size="small" @click="deletesa(row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <div>
            <el-dialog v-model="dialogVisible" :title="title" width="500" :before-close="handleClose">
                <div style="display: flex;justify-content: space-around;align-items: center;">
                    <el-steps direction="vertical" :active="active">
                        <el-step :title="stepname" v-for="(stepname, index) in salaryItemName" />
                    </el-steps>
                    <el-input v-for="(value, key, index) in  salary" v-model="salary[key]"
                        :placeholder="'请输入' + salaryItemName[index]" :key="index" v-show="active == index"
                        style="width: 200px; height: 40px;" />
                </div>
                <template #footer>
                    <div class="dialog-footer">
                        <el-button @click="next1">{{ active == 0 ? '取消' : '上一步' }}</el-button>
                        <el-button type="primary" @click="next2">
                            {{ active == 10 ? '完成' : '下一步' }}
                        </el-button>
                    </div>
                </template>
            </el-dialog>
        </div>
    </div>
</template>

<script  setup>
import { ref, onMounted } from 'vue';
import { getRequest, postRequest, deleteRequest,putRequest } from '@/utils/api';
import { ElMessageBox, ElMessage } from 'element-plus';

const emp = ref();
const dialogVisible = ref(false);
const active = ref(0);
const salaryItemName = ['账套名称', '基本工资', '交通补助', '午餐补助', '奖金', '养老金比率', '养老金基数', '医疗保险比率', '医疗保险基数', '公积金比率', '公积金基数']
const nextstep = '上一步';
const nextstep2 = '下一步';
const title = ref('');
const salary = ref({
    name: '', basicSalary: 0, trafficSalary: 0,
    lunchSalary: 0, bonus: 0, pensionPer: 0, pensionBase: 0, medicalPer: 0, medicalBase: 0, accumulationFundPer: 0, accumulationFundBase: 0
})

const findAll = onMounted(() => {
    getRequest('/salary/sob/').then((res) => {
        emp.value = res;
    });
})
//上一步
const next1 = () => {
    if (active.value == 0) {
        dialogVisible.value = false;
        return;
    }
    active.value = active.value - 1;
}
//下一步
const next2 = () => {
    active.value = active.value + 1;
    if (active.value == 11) {
        if (salary.value.id) {
            putRequest('/salary/sob/', salary.value).then((res) => {
                findAll();
            })
            dialogVisible.value = false;
            active.value = 0;
        }
        else {
            postRequest('/salary/sob/', salary.value).then((res) => {
                findAll();
            })
            dialogVisible.value = false;
            active.value = 0;
        }

        salary.value = {
            name: '', basicSalary: 0, trafficSalary: 0,
            lunchSalary: 0, bonus: 0, pensionPer: 0, pensionBase: 0, medicalPer: 0, medicalBase: 0, accumulationFundPer: 0, accumulationFundBase: 0
        }
    }

}

//删除
const deletesa = (row) => {
    ElMessageBox.confirm(
        '您确定要删除【' + row.name + '】吗？',
        'Warning',
        {
            confirmButtonText: 'OK',
            cancelButtonText: 'Cancel',
            type: 'warning',
        }
    )
        .then(() => {
            deleteRequest('/salary/sob/' + row.id).then((res) => {
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
//编辑
const edit = (data) => {
    title.value = '编辑工资账套';
    active.value = 0;
    salary.value.id = data.id;
    salary.value.name = data.name;
    salary.value.basicSalary = data.basicSalary;
    salary.value.trafficSalary = data.trafficSalary;
    salary.value.lunchSalary = data.lunchSalary;
    salary.value.bonus = data.bonus;
    salary.value.pensionPer = data.pensionPer;
    salary.value.pensionBase = data.pensionBase;
    salary.value.medicalPer = data.medicalPer;
    salary.value.medicalBase = data.medicalBase;
    salary.value.accumulationFundPer = data.accumulationFundPer;
    salary.value.accumulationFundBase = data.accumulationFundBase;
    dialogVisible.value = true;
}
//添加
const toadd = () => {
    title.value = '添加工资账套';
    dialogVisible.value = true;
}
</script>

<style ></style>
