<template>
    <div>
        <el-form ref="registerForm" :model="form" :rules="rules" label-width="100px" class="resiger-box">
        <el-form-item label="账号" prop="username">
            <span type="text" style="width: 80%;" readonly v-text="form.username"/>
        </el-form-item>
        <el-form-item label="初始化密码">
            <el-switch v-model="form.password"></el-switch>
        </el-form-item>
        <el-form-item label="性别" >
            <el-radio-group v-model="form.sex">
                <el-radio :label="1" >男</el-radio>
                <el-radio :label="0" >女</el-radio>
                <el-radio :label="2" selected>保密</el-radio>
            </el-radio-group>
        </el-form-item>
        <el-form-item label="昵称" >
            <el-input type="text" style="width: 80%;" v-model="form.nickname"/>
        </el-form-item>
        <el-form-item label="邮箱" prop="mail">
            <el-input type="text" style="width: 80%;" v-model="form.mail"/>
        </el-form-item>
        <el-form-item label="电话" >
            <el-input type="text" style="width: 80%;" v-model="form.tel"/>
        </el-form-item>
        </el-form>
    </div>
</template>

<script>
import { Message } from 'element-ui'
import { validateEMail,updateUser } from '@/api/user';
export default {
    name: "editUser",
    props: {
        editForm: {
            type: Object,
            default: {}
        }
    },
    mounted(){
        this.form =  {
                username: this.editForm.username,
                password: false,
                nickname: this.editForm.nickname,
                sex: this.editForm.sex,
                state: this.editForm.state,
                mail:this.editForm.mail,
                tel: this.editForm.tel
            };
    },
    data() {
        var validateMail = (rules, value, callback) => {
            var reg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
            if (value === '') {
                callback(new Error('邮箱格式错误！'));
            } else if (!reg.test(value)) {
                callback(new Error('邮箱格式错误!'));
            }
            else {
                if(value == this.form.mail){
                    callback()
                } else{
                    validateEMail(value).then(response=>{
                      if(response.object){
                          callback();
                      }  
                    }).catch(()=>{
                        callback(new Error('邮箱已存在!'));
                    });
                }
            }
        };
        return {
            form: {},
            // 表单验证，需要在 el-form-item 元素中增加 prop 属性
            rules: {
                mail: [
                    { validator: validateMail, trigger: 'blur' },
                    {required: true, trigger: 'blur'},
                ],
            }
        }
    },
    methods: {
        reset(){
            this.form =  {
                username: this.editForm.username,
                password: false,
                nickname: this.editForm.nickname,
                sex: this.editForm.sex,
                state: this.editForm.state,
                mail:this.editForm.mail,
                tel: this.editForm.tel
            };
        },
        onSubmit(formName) {
            // 为表单绑定验证功能
            return new Promise((resolve, reject) => {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        updateUser(this.form)
                            .then(()=>{
                                Message.success("修改成功")
                                this.reset();
                                resolve()
                            })
                            .catch(reject => {
                                Message.error("修改失败")
                                this.reset();
                                resolve()
                            })
                    } else {
                        reject()
                    }
                });
            })
        }
    },
}
</script>