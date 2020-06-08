<template>
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
    <el-form-item label="编码">
        <span v-text="ruleForm.id"/>
    </el-form-item>
    <el-form-item label="名称" prop="menuName">
        <el-input :disabled="ruleForm.updateEnable" v-model="ruleForm.menuName"></el-input>
    </el-form-item>
    <el-form-item label="链接" prop="url">
        <el-input :disabled="ruleForm.updateEnable" placeholder="请输入内容" v-model="ruleForm.url">
            <template slot="prepend">{{ ruleForm.parentPath==null?ruleForm.parentPath:(ruleForm.parentPath+'/') }}</template>
        </el-input>
    </el-form-item>
    <el-form-item label="组件">
        <el-input :disabled="ruleForm.updateEnable" placeholder="请输入内容，默认为Layout" v-model="ruleForm.component"></el-input>
    </el-form-item>
    <el-form-item label="重定向链接" >
        <el-input :disabled="ruleForm.updateEnable" placeholder="请输入内容" v-model="ruleForm.redirect"></el-input>
    </el-form-item>
    <el-form-item label="隐藏">
        <el-switch :disabled="ruleForm.updateEnable" v-model="ruleForm.hidden"></el-switch>
    </el-form-item>
    <el-form-item label="标签页固定">
        <el-switch :disabled="ruleForm.updateEnable" v-model="ruleForm.affix"></el-switch>
    </el-form-item>
    <el-form-item label="图标">
        <el-input :disabled="ruleForm.updateEnable" placeholder="请输入SVG-icon的名称。根菜单不想出现名字，则icon为空。" v-model="ruleForm.icon"></el-input>
    </el-form-item>
    <el-form-item label="描述" prop="description">
        <el-input type="textarea" :disabled="ruleForm.updateEnable" v-model="ruleForm.description"></el-input>
    </el-form-item>
    <el-form-item>
        <el-button :disabled="ruleForm.updateEnable" type="primary" @click="submitForm('ruleForm')">修改</el-button>
        <el-button :disabled="ruleForm.updateEnable" @click="resetForm">重置</el-button>
    </el-form-item>
    </el-form>
</template>
<script>
import { getMenuData,setMenuData } from '@/api/menu'
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
        ruleForm: {
            "id": '',
            "menuName": '',
            "url": '',
            "component": 'Layout',
            "redirect": null,
            "icon": null,
            "pid": null,
            "description": null,
            "affix": false,
            "menuSort": null,
            "hidden": false,
            "updateEnable": true,
            "parentPath": null
        },
        rules: {
          menuName: [
            { required: true, message: '请输入菜单名称', trigger: 'blur' },
            { min: 1, max: 12, message: '长度在 1 到 12 个字符', trigger: 'blur' }
          ],
          url: [
            { required: true, message: '请输入URL', trigger: 'blur' }
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
            setMenuData(this.ruleForm).then(r=>{
                this.$emit('func')
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
          getMenuData(id).then(r=>{
              if(r.object==null){
                  this.$message.error('未找到数据')
              }else{
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
                        this.$confirm(`确定切换菜单?`, '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                        }).then(()=>{
                            this.getData(newVal);
                        }).catch(()=>{})
                }else{
                    this.ruleForm= {
                        "id": '',
                        "menuName": '',
                        "url": '',
                        "component": 'Layout',
                        "redirect": null,
                        "icon": null,
                        "pid": null,
                        "description": null,
                        "affix": false,
                        "menuSort": null,
                        "hidden": false,
                        "updateEnable": true,
                        "parentPath": null
                    }
                }
            }
        }
    }
}
</script>
<style scoped>

</style>