<template>
    <div>
        <div v-text="'选中角色:'+roleName" style="margin-bottom:10px;color:Silver;"></div>
        <el-input size="mini" style="margin-bottom:10px"
        placeholder="输入菜单名查询"
        v-model="filterText">
        </el-input>
        <el-tree
        :data="data"
        :show-checkbox="roleId!=0"
        node-key="id"
        ref="tree"
        :props="defaultProps"
        :filter-node-method="filterNode">
        </el-tree>
        <el-divider><i class="el-icon-brush"></i></el-divider>
        <el-button type="text" :disabled="roleId==0" @click="getMenu">刷新</el-button>
        <div class="bottom">
            <el-button type="text" @click="addChecked" :disabled="roleId==0">提交</el-button>
        </div>
    </div>
</template>
<script>
import { getMenuLabel } from '@/api/menu'
import { addRoleMenu,getRoleMenuChecked } from '@/api/role'
export default {
    props: {
        roleName: {
            type: String,
            default: '无'
        },
        roleId: {
          type: Number,
          default: 0
        }
    },
    mounted(){
        this.getMenu()
    },
    data(){
        return{
            filterText: '',
            data:[],
            defaultProps: {
                children: 'children',
                label: 'label'
            }
        }
    },
    methods:{
      filterNode(value, data) {
          if (!value) return true;
          return data.label.indexOf(value) !== -1;
      },
      getMenu(){
          getMenuLabel().then(r=>{
              console.log('菜单')
              console.log(r.object)
              this.data=r.object.data
          }).catch(()=>{})
          if(this.roleId!=0){
            this.getMenuChecked(this.roleId)
            this.$message.success('刷新成功')
          }
      },
      getMenuChecked(val){
        getRoleMenuChecked(val).then(r=>{
          const val = r.object;
          let checked =[];
          val.forEach(element => {
            if(this.data.findIndex(e=>e.id == element.menuId)==-1){
              checked.push(element.menuId)
            }
          });
          //console.log(checked)
          this.$refs.tree.setCheckedKeys(checked);
        }).catch()
      },
      addChecked(){
        console.log(this.$refs.tree.getCheckedKeys());
        let checked = this.$refs.tree.getCheckedKeys().concat(this.$refs.tree.getHalfCheckedKeys());
        addRoleMenu(checked,this.roleId).then(r=>{
          this.$message.success("修改成功")
        }).catch()
      },

      
    },
    watch: {
      filterText(val) {
        this.$refs.tree.filter(val);
      },
      'roleId': function(newVal){
        this.getMenuChecked(newVal);
      }
    },
}
</script>
<style scoped>
  .bottom {
    /* margin-top: 13px;
    line-height: 12px; */
    float: right;
    margin-bottom: 14px;
  }
  .el-divider--horizontal{
     margin: 20px 0;
     background: 0 0;
     border-top: 1px dashed #e8eaec;
  } 
  .custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    padding-right: 8px;
  }
</style>