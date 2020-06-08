<template>
    <div>
      <el-container>

        <el-main style="width:70%">
        <div style="margin-bottom:20px;margin-top:20px">组申请</div>
        <el-card>
            <el-table
            ref="filterTable"
            :data="data"
            style="width: 100%;">
                <el-table-column
                label="组名">
                  <template slot-scope="scope">
                    {{ scope.row.name }}
                  </template>
                </el-table-column>
                <el-table-column 
                prop="group_name"
                label="系统组名">
                </el-table-column>
                <el-table-column 
                prop="serverId"
                label="所属服务器">
                  <template slot-scope="scope">
                    {{ scope.row.serverId+'号服务器' }}
                  </template>
                </el-table-column>
                <el-table-column
                label="采用方式">
                  <template slot-scope="scope">
                    {{ scope.row.filter==true?'过滤危险命令行':'允许部分命令行' }}
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="240">
                    <template slot-scope="scope">
                        <el-popconfirm 
                            v-if="scope.row.apply_status==0?true:false"
                            style="margin-left:10px"
                            placement="top"
                            :title="`确定申请${scope.row.name}吗？只能拥有一个组！切换组后会退出原来组`"
                            @onConfirm="apply(scope.row.id)"
                        >
                            <el-button slot="reference" type="success" size="mini">申请</el-button>
                        </el-popconfirm>
                        <el-popconfirm 
                            v-if="scope.row.apply_status!=0?true:false"
                            style="margin-left:10px"
                            placement="top"
                            :title="`确定撤回${scope.row.name}的申请吗？`"
                            @onConfirm="del(scope.row.id)"
                        >
                            <el-button slot="reference" type="danger" size="mini">撤回</el-button>
                        </el-popconfirm>
                        <!-- <el-tag style="margin-left:10px;"  type="danger" size="mini">申请中</el-tag> -->
                    </template>
                </el-table-column>
            </el-table>
        </el-card>
      </el-main>
      </el-container>
    </div>
</template>
<script>
import { applyGroupList,applyGroup,delApply } from '@/api/group'
export default {
    mounted(){
        this.getData()
    },
    data(){
        return {
            data:[],
        }
    },
    methods:{
      getData(){
        applyGroupList(this.$store.getters.username,this.$store.getters.groupId).then(r=>{
          console.log(r.object)
          this.data=r.object
        }).catch()
      },
      apply(id){
          applyGroup(id,this.$store.getters.username).then(r=>{
              this.getData()
              this.$message.success('申请成功')
          }).catch()
      },
      del(id){
          delApply(id,this.$store.getters.username).then(r=>{
              this.getData()
              this.$message.success('撤回成功')
          }).catch()
      }
    }
    
}
</script>
<style lang="scss" scoped>
  .time {
    font-size: 13px;
    color: #999;
  }
  
  .bottom {
    margin-top: 13px;
    line-height: 12px;
  }

  .button {
    padding: 0;
    float: right;
  }

  .image {
    width: 100%;
    display: block;
  }

  .clearfix:before,
  .clearfix:after {
      display: table;
      content: "";
  }
  
  .clearfix:after {
      clear: both
  }


  .panel-group {
  margin-top: 18px;

  .card-panel-col {
    margin-bottom: 32px;
  }

}
</style>