<!--
 * new page
 * @author: sx
 * @since: 2024-02-23
 * userinfo.vue
-->
<template>
  <div class="container">
    <el-card style="width: 400px;">
      <template #header>
        <div class="card-header">
          <span>{{ admin.name }}</span>

        </div>
      </template>
      <div>
        <div style="display: flex;justify-content: center;margin-bottom: 10px;">
          <el-upload :headers="headers"  :data="admin" :show-file-list="false" :on-success="onSuccess" action="/admin/userface">
            <img :src="admin.userFace" style="width: 90px;height: 90px;border-radius: 90px;">
          </el-upload>
        </div>
        <div>
          <div>电话号码：
            <el-tag>{{ admin.phone }}</el-tag>
          </div>
          <div>手机号码：
            <el-tag>{{ admin.telephone }}</el-tag>
          </div>
          <div>居住地址：
            <el-tag>{{ admin.address }}</el-tag>
          </div>
          <div>用户标签：
            <el-tag type="success" v-for="r in admin.roles">{{ r.nameZh }}</el-tag>
          </div>
          <div style="display: flex;justify-content:space-around;margin-top: 10px;">
            <el-button class="button" type="primary" @click="updateinfo">更新资料</el-button>
            <el-button class="button" type="danger" @click="updateps">修改密码</el-button>
          </div>
        </div>
      </div>
    </el-card>

    <div>
      <el-dialog v-model="dialogVisible" title="更新资料" width="500" :before-close="handleClose">
        <div>
          <el-form :model="admin2" label-width="80px">
            <el-form-item label="用户名">
              <el-input v-model="admin2.name"></el-input>
            </el-form-item>
            <el-form-item label="电话号码">
              <el-input v-model="admin2.phone"></el-input>
            </el-form-item>
            <el-form-item label="手机号码">
              <el-input v-model="admin2.telephone"></el-input>
            </el-form-item>
            <el-form-item label="居住地址">
              <el-input v-model="admin2.address"></el-input>
            </el-form-item>
          </el-form>
        </div>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary" @click="toupdate">
              确定
            </el-button>
          </div>
        </template>
      </el-dialog>
    </div>

    <!-- 修改密码 -->
    <div>
      <el-dialog v-model="dialogVisible2" title="修改密码" width="500" :before-close="handleClose">
        <div>
          <el-form ref="ruleFormRef" :model="ruleForm" label-width="80px" :rules="rules">
            <el-form-item label="原密码" prop="oldpass">
              <el-input v-model="ruleForm.oldpass"></el-input>
            </el-form-item>
            <el-form-item label="新密码" prop="pass">
              <el-input v-model="ruleForm.pass"></el-input>
            </el-form-item>
            <el-form-item label="确认密码" prop="checkPass">
              <el-input v-model="ruleForm.checkPass"></el-input>
            </el-form-item>
          </el-form>
        </div>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="dialogVisible2 = false">取消</el-button>
            <el-button type="primary" @click="toupdatepass(ruleFormRef)">
              确定
            </el-button>
          </div>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script setup >
import { ref, onMounted, reactive } from 'vue'
import { getRequest, putRequest } from '@/utils/api';
import store from '@/store';
import router from '@/router';
const admin = ref({})
const admin2 = ref({})
const dialogVisible = ref(false)
const dialogVisible2 = ref(false)
const headers = {
  Authorization: window.sessionStorage.getItem('tokenStr')
}

const ruleFormRef = ref()
const ruleForm = reactive({
  pass: '',
  checkPass: '',
  oldpass: '',
  adminId: ''
})
const validatePass = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请输入密码'))
  } else {
    if (ruleForm.checkPass !== '') {
      if (!ruleFormRef.value) return
      ruleFormRef.value.validateField('checkPass', () => null)
    }
    callback()
  }
}
const validatePass2 = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== ruleForm.pass) {
    callback(new Error("两次输入密码不一致"))
  } else {
    callback()
  }
}

// 表单验证
const rules = reactive({
  pass: [{ validator: validatePass, trigger: 'blur' }],
  checkPass: [{ validator: validatePass2, trigger: 'blur' }],
  oldpass: [{ required: true, message: '请输入原来密码', trigger: 'blur' }],
})

const findAll = onMounted(() => {
  getRequest('/admin/info').then((res) => {
    admin.value = res
    Object.assign(admin2.value, res)
    window.localStorage.setItem('user', JSON.stringify(res))
    console.log(window.localStorage.getItem('user'));
    store.commit('initadmin', res)
    console.log(store.state.currentAdmin);
  })
})
// 更新资料
const updateinfo = () => {
  dialogVisible.value = true
}
// 修改密码
const updateps = () => {
  dialogVisible2.value = true
}
const toupdate = () => {
  putRequest('/admin/info', admin2.value).then((res) => {
    dialogVisible.value = false
    findAll()
  })
  dialogVisible.value = false
}

// 修改密码
const toupdatepass = (ruleFormRef) => {
  if (!ruleFormRef) return
  ruleFormRef.validate((valid) => {
    if (valid) {
      ruleForm.adminId = admin.value.id
      putRequest('/admin/pass', ruleForm).then((res) => {
        dialogVisible2.value = false
        window.sessionStorage.clear();
        store.commit('initRoutes', []);
        router.push('/');
      })
    } else {
      console.log('error submit!')
      return false
    }
  })
}
//上传成功
const onSuccess = () => {
    findAll()
}


</script>

<style scoped></style>
