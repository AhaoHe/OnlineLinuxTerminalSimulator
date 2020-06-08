<template>
    <el-container>
  <el-header>
      <header-home></header-home>
  </el-header>
  <el-main>
    <div :style ="note" >
        <el-form ref="registerForm" :model="form" :rules="rules" label-width="100px" class="resiger-box">
        <el-page-header @back="goBack" class="resiger-title" content="注册页面" />
        <!-- <h3 class="login-title">注册</h3> -->
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
        <el-form-item label="验证码" prop="verCode">
            <el-input type="text" style="width: 40%;" v-model="form.verCode"/>
            <el-button :type="style" style="width: 40%;" @click.native.prevent="sendEmail()" v-text="buttonValue" :disabled='disabled'></el-button>
        </el-form-item>
        <el-form-item label="电话" >
            <el-input type="text" style="width: 80%;" v-model="form.tel"/>
        </el-form-item>
        <!-- <el-form-item label="服务器" >
            <el-select v-model="form.server" placeholder="活动区域">
                <el-option label="服务器1" value="1"></el-option>
                <el-option label="服务器2" value="2"></el-option>
            </el-select>
        </el-form-item> -->
        <el-form-item>
            <el-button type="primary" @click.native.prevent="onSubmit('registerForm')" :loading="loading" 
            style="margin:0 auto;width:200px;">注册</el-button>
        </el-form-item>
        </el-form>

        <el-dialog
        title="温馨提示"
        :visible.sync="dialogWarnVisible"
        width="30%">
        <span >带'*'的内容必填</span>
        <span slot="footer" class="dialog-footer">
            <el-button type="primary" @click="dialogWarnVisible = false">确 定</el-button>
        </span>
        </el-dialog>
    </div>
  </el-main>
  <el-footer>
    <footer-home></footer-home>
  </el-footer>
</el-container>
</template>
<script>
import headerHome from './components/headerHome';
import footerHome from './components/footerHome';
import { validateEMail,sendMail,register,validateName } from '@/api/user';
import { Message } from 'element-ui'
export default {
    name: "Register",
    data() {
        var validateUsername = (rules, value, callback) => {
            if(value=='') callback(new Error('账号不能为空!'))
            var patrn=/^[a-zA-Z][a-zA-Z0-9]{4,20}$/;  
            if (!patrn.exec(value)) callback(new Error('只能输入4-20个以字母开头的字母、数字组合!'));
            else{
                validateName(value).then(response=>{
                  if(response.object){
                      callback();
                  }  
                }).catch(()=>{
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
                this.mailEnable = false
                callback(new Error('邮箱格式错误！'));
            } else if (!reg.test(value)) {
                this.mailEnable = false
                callback(new Error('邮箱格式错误!'));
            } else {
                validateEMail(value).then(response=>{
                  //const { ,description} = response.data.object;
                  console.log(response.object)
                  if(response.object){
                      this.mailEnable = true
                      callback();
                  }  
                }).catch(()=>{
                    callback(new Error('邮箱已存在!'));
                });
            }
        };
        var verCodeLimit = (rules, value, callback) => {
            if(value.length!=6)
                callback(new Error('请输入六位验证码!'));
            else
                callback();
        };
        return {
            note: {
                backgroundImage: "url(" + require("@/assets/background.jpg") + ") ",
                //background: "#F0F8FF",
                backgroundPosition: "center center",
                backgroundRepeat: "no-repeat",
                backgroundSize: "cover" ,
            },
            form: {
                username: '',
                password: '',
                checkPassword: '',
                nickname:'',
                sex: 2,
                mail:'',
                tel: '',
                verCode: ''
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
                verCode:[
                    { validator: verCodeLimit, trigger: 'blur' },
                    {required: true, message: '验证码不能为空', trigger: 'blur'},
                ]
            },
            buttonValue: '获取验证码',
            count: 60,
            disabled:false,
            mailEnable: false,
            style:'primary',
            // 对话框显示和隐藏
            dialogWarnVisible: false,
            loading: false,
        }
    },
    methods: {
        sendEmail(){
            if(!this.mailEnable){
                Message.error('邮箱未验证！请填写邮箱后点击空白处验证!')
                return false;
            }
            var countDown = setInterval(()=>{
                    if(this.count < 1){
                        this.disabled = false;
                        this.style = 'primary';
                        this.buttonValue = '获取验证码';
                        this.count = 60;
                        clearInterval(countDown);
                    }else{
                        this.disabled = true;
                        this.style = 'info';
                        this.buttonValue = this.count-- + 's后重发';
                    }
            },1000);
            sendMail(this.form.mail).then(response=>{
                Message.success("发送成功");
            }).catch(()=>{
                Message.success("发送失败");
            })
            this.mailEnable=false;
        },
        onSubmit(formName) {
            // 为表单绑定验证功能
            this.$refs[formName].validate((valid) => {
                if (valid) {
                this.loading = true //禁止再次点击按钮
                register(this.form)
                    .then(()=>{
                        Message.success("注册成功")
                        this.$router.replace({
                            name: 'Login'
                        })
                        this.loading = false
                        })
                    .catch(reject => {
                        this.loading = false
                    })
                
                } else {
                this.dialogWarnVisible = true;
                return false;
                }
            });
        },
        goBack(){
            this.$router.replace('/login')
        }

    },
    components:{
        headerHome,
        footerHome
    }
}
</script>

<style lang="scss" scoped>
  .resiger-box {
    border: 1px solid #DCDFE6;
    width: 450px;
    margin: 20px auto;
    padding: 35px 35px 15px 35px;
    border-radius: 5px;
    -webkit-border-radius: 5px;
    -moz-border-radius: 5px;
    box-shadow: 0 0 25px #909399;
  }
  .resiger-title {
    margin: 0 auto 40px auto;
    color: #303133;
  }
  .el-container {
    margin-top: 60px;
  }
 
</style>