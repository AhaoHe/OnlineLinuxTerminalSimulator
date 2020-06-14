<template >
<el-container>
  <el-header>
      <header-home></header-home>
  </el-header>
  <el-main>
    <div :style ="note" >
        <el-form ref="loginForm" :model="form" :rules="rules" label-width="100px" class="login-box">
        <h3 class="login-title">欢迎登录</h3>
        <el-form-item label="账号" prop="username">
            <el-input type="text" style="width: 80%;" placeholder="请输入账号" v-model="form.username"/>
        </el-form-item>
        <el-form-item label="密码" prop="password">
            <el-input type="password" style="width: 80%;" placeholder="请输入密码" v-model="form.password"
            @keyup.enter.native.prevent="onSubmit('loginForm')"/>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click.native.prevent="onSubmit('loginForm')" :loading="loading" >登录</el-button>
            <el-button @click.native.prevent="gotoRegister">注册</el-button>
            <el-button type="text">忘记密码？</el-button>
        </el-form-item>
        </el-form>

        <el-dialog
        title="温馨提示"
        :visible.sync="dialogWarnVisible"
        width="30%">
        <span >请输入账号和密码</span>
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
export default {
  name: "Login",
  data() {
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
        password: ''
      },
      // 表单验证，需要在 el-form-item 元素中增加 prop 属性
      rules: {
        username: [
          {required: true, message: '账号不可为空', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '密码不可为空', trigger: 'blur'}
        ]
      },
      // 对话框显示和隐藏
      dialogWarnVisible: false,
      loading: false,
    }
  },
  methods: {
    onSubmit(formName) {
      // 为表单绑定验证功能
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.loading = true //禁止再次点击按钮
          // 使用 vue-router 路由到指定页面，该方式称之为编程式导航
          //this.$router.push("/main");
          /*this.$axios.post(
            '/login',
            {
              username:this.form.username,
              password:this.form.password
          })
          .then(resultResponse => {
            this.resultResponse = JSON.stringify(resultResponse.data)
            //成功后执行 带着用户名跳转到Main页面
            if(resultResponse.data.code === 200) {
              this.$router.replace({
                name:'Main',
                params:{
                  username :resultResponse.data.object.username
                }
                })
            }else if(resultResponse.data.code === 400) {
                this.dialogErrorVisible = true;
                this.error = resultResponse.data.msg;
            }
          })
          .catch(failResponse => {})*/


          this.$store.dispatch('user/login', this.form)
            .then(()=>{
              this.$router.replace({
                name: '首页'
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
    gotoRegister(){
      this.$router.replace('/resgister')
    }

  },
  components:{
    headerHome,
    footerHome
  }
}
</script>

<style lang="scss" scoped>
  .login-box {
    border: 1px solid #DCDFE6;
    width: 450px;
    margin: 20px auto;
    padding: 35px 35px 15px 35px;
    border-radius: 5px;
    -webkit-border-radius: 5px;
    -moz-border-radius: 5px;
    box-shadow: 0 0 25px #909399;
  }
  .login-title {
    text-align: center;  
    margin: 0 auto 40px auto;
    color: #303133;
  }
  .el-container {
    margin-top: 60px;
  }
 
</style>