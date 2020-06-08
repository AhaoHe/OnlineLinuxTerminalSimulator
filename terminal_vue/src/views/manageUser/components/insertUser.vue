<template>
    <div>
        <el-form ref="registerForm" :model="form" :rules="rules" label-width="100px" class="resiger-box">
        <el-form-item label="账号" prop="username">
            <el-input type="text" style="width: 80%;" placeholder="请输入账号" v-model="form.username"/>
        </el-form-item>
        <el-form-item label="密码" prop="password">
            <el-input type="password" style="width: 80%;" placeholder="请输入密码" v-model="form.password"/>
        </el-form-item>
        <el-form-item label="确认密码" prop="checkPassword">
            <el-input type="password" style="width: 80%;" placeholder="再次输入密码" v-model="form.checkPassword"/>
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
import { validateEMail,validateName,addUser } from '@/api/user';
export default {
    name: "InsertUser",
    data() {
        var validateUsername = (rules, value, callback) => {
            if(value=='') callback(new Error('账号不能为空!'))
            var patrn=/^[a-zA-Z][a-zA-Z0-9]{4,20}$/;  
            if (!patrn.exec(value)){
                callback(new Error('只能输入4-20个以字母开头的字母、数字组合!'));
            }
            else {
                validateName(value).then(response=>{
                  if(response.object){
                      callback();
                  }
                }).catch(reject=>{
                    callback(new Error('用户名已存在!'));
                });
            }
        }
        var validatePass = (rules, value, callback) => {
            if(value=='') callback(new Error('密码不能为空!'))
            var patrn=/^(\w){6,20}$/;  
            if (!patrn.exec(value)) callback(new Error('只能输入6-20个字母、数字、下划线!'));
            else callback()
        }
        var validateCheckPass = (rules, value, callback) => {
            if (value === '') {
                callback(new Error('请再次输入密码'));
            } else if (value !== this.form.password) {
                callback(new Error('两次输入密码不一致!'));
            } else {
                callback();
            }
        };
        var validateMail = (rules, value, callback) => {
            var reg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
            if (value === '') {
                callback(new Error('邮箱格式错误！'));
            } else if (!reg.test(value)) {
                callback(new Error('邮箱格式错误!'));
            } else {
                validateEMail(value).then(response=>{
                  if(response.object){
                      callback();
                  }  
                }).catch(()=>{
                    callback(new Error('邮箱已存在!'));
                });
            }
        };
        return {
            form: {
                username: '',
                password: '',
                checkPassword: '',
                nickname:'',
                sex: 2,
                mail:'',
                tel: ''
            },
            // 表单验证，需要在 el-form-item 元素中增加 prop 属性
            rules: {
                username: [
                    { validator: validateUsername, trigger: 'blur' },
                    {required: true, message: '账号不可为空', trigger: 'blur'},
                    //{ min: 4, max: 12, message: '长度在 4 到 12 个字符', trigger: 'blur' }
                ],
                password: [
                    { validator: validatePass, trigger: 'blur' },
                    {required: true, message: '密码不可为空', trigger: 'blur'},
                    //{ min: 4, max: 12, message: '长度在 4 到 12 个字符', trigger: 'blur' }
                ],
                checkPassword: [
                    { validator: validateCheckPass, trigger: 'blur' },
                    {required: true, trigger: 'blur'},
                ],
                mail: [
                    { validator: validateMail, trigger: 'blur' },
                    {required: true, trigger: 'blur'},
                ],
            }
        }
    },
    methods: {
        reset(){
            this.form={
                username: '',
                password: '',
                checkPassword: '',
                nickname:'',
                sex: 2,
                mail:'',
                tel: ''
            };
        },
        onSubmit(formName) {
            // 为表单绑定验证功能
            return new Promise((resolve, reject) => {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        addUser(this.form)
                            .then(()=>{
                                Message.success("添加成功")
                                this.form={
                                    username: '',
                                    password: '',
                                    checkPassword: '',
                                    nickname:'',
                                    sex: 2,
                                    mail:'',
                                    tel: ''
                                };
                                resolve()
                            })
                            .catch(reject => {
                                Message.error("添加失败")
                                this.form={
                                    username: '',
                                    password: '',
                                    checkPassword: '',
                                    nickname:'',
                                    sex: 2,
                                    mail:'',
                                    tel: ''
                                };
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