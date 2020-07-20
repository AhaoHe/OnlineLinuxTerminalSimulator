<template>
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
    <el-form-item label="编码">
        <span v-text="ruleForm.id"/>
    </el-form-item>
    <el-form-item label="名称" prop="apiName">
        <el-input :disabled="ruleForm.enable" v-model="ruleForm.apiName"></el-input>
    </el-form-item>
    <el-form-item label="api" prop="apiUrl">
        <el-input :disabled="ruleForm.enable" placeholder="请输入内容" v-model="ruleForm.apiUrl">
            <template slot="prepend">{{ ruleForm.parentPath }}</template>
        </el-input>
    </el-form-item>
    <el-form-item label="方法method">
        <el-checkbox-group v-model="ruleForm.apiMethod" :disabled='ruleForm.pid==0'>
            <el-checkbox label="GET" name="type"></el-checkbox>
            <el-checkbox label="POST" name="type"></el-checkbox>
            <el-checkbox label="PUT" name="type"></el-checkbox>
            <el-checkbox label="DELETE" name="type"></el-checkbox>
        </el-checkbox-group>
    </el-form-item>
    <el-form-item label="描述" prop="description">
        <el-input type="textarea" :disabled="ruleForm.enable" v-model="ruleForm.description"></el-input>
    </el-form-item>
    <el-form-item>
        <el-button :disabled="ruleForm.enable" type="primary" @click="submitForm('ruleForm')">修改</el-button>
        <el-button :disabled="ruleForm.enable" @click="resetForm">重置</el-button>
    </el-form-item>
    </el-form>
</template>
<script>
import { getApiData,setApiData } from '@/api/api'
export default {
    name:'DataForm',
    props: {
        dataFormId: {
            type: Number,
            default: 0
        }
    },
    data() {
      return {
        ruleForm:  {
            "id": '',
            "apiName": '',
            "apiUrl": '',
            "apiMethod": '',
            "pid": 0,
            "apiSort": 0,
            "description": null,
            "enable": false,
            "parentPath": ''
        },
        rules: {
          apiName: [
            { required: true, message: '请输入API名称', trigger: 'blur' },
            { min: 1, max: 12, message: '长度在 1 到 12 个字符', trigger: 'blur' }
          ],
          apiUrl: [
            { required: true, message: '请输入API', trigger: 'blur' }
          ],
          description: [
            { max: 100, message: '长度小于 100 个字符', trigger: 'blur' }
          ],
        }
      };
    },
    methods: {
      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.ruleForm.apiMethod=this.ruleForm.apiMethod.filter(i=>i!='').join(',')
            console.log(this.ruleForm)
            setApiData(this.ruleForm).then(r=>{
                this.$emit('func')
                this.ruleForm.apiMethod=this.ruleForm.apiMethod.split(',');
                this.$message.success("修改成功")
            }).catch(()=>{})
          } else {
            this.$message.error("缺少参数或参数错误")
            return false;
          }
        });
      },
      resetForm() {
        this.getData(this.ruleForm.id);
      },
      getData(id){
          getApiData(id).then(r=>{
              if(r.object==null){
                  this.$message.error('未找到数据')
              }else{
                  r.object.apiMethod = r.object.apiMethod.split(',');
                  this.ruleForm=r.object
                  console.log(r.object)
              }
          }).catch(()=>{})
      }
    },
    watch:{
        'dataFormId':function(newVal){
            if(this.ruleForm.id==''){
                this.getData(newVal);
            }else{
                if(newVal!=0){
                        this.$confirm(`确定切换API?`, '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                        }).then(()=>{
                            this.getData(newVal);
                        }).catch(()=>{})
                }else{
                    this.ruleForm= {
                        "id": '',
                        "apiName": '',
                        "apiUrl": '',
                        "apiMethod": '',
                        "pid": 0,
                        "apiSort": 0,
                        "description": null,
                        "enable": false,
                        "parentPath": ''
                    }
                }
            }
        }
    }
}
</script>
<style scoped>

</style>