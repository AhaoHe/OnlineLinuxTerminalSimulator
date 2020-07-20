<template>
    <el-tabs type="border-card" >
        <el-tab-pane label="历史代码">
            <el-table
            :data="data">
                <el-table-column 
                prop="username"
                label="用户名">
                </el-table-column>
                <el-table-column 
                prop="serverId"
                label="服务器">
                <template slot-scope="scope">
                    {{ `${scope.row.serverId}号` }}
                </template>
                </el-table-column>
                <el-table-column 
                prop="command"
                label="命令行">
                <template slot-scope="scope">
                    <span :style="`color:${scope.row.danger==1?'red':'green'}`">{{ `${scope.row.command}` }}</span>
                </template>
                </el-table-column>
                <el-table-column 
                prop="time"
                label="时间"
                width="200">
                </el-table-column>
            </el-table>
            <div class="block" style="text-align: center;">
                <el-pagination
                @current-change="getCommandData"
                :current-page.sync="query.current"
                :page-size="query.size"
                layout="total, prev, pager, next, jumper"
                :total="total">
                </el-pagination>
            </div>
        </el-tab-pane>
        <el-tab-pane label="统计图" >
            <el-card>
            <e-charts :lazy="true" :width="'500px'" :chart-data="ChartData" ></e-charts>
            </el-card>
        </el-tab-pane>
    </el-tabs>
</template>
<script>
import ECharts from '@/components/ECharts'
import { getUserChart,getHistoryCommand } from '@/api/home'
export default {
    props:{
        userName:{
            type:String,
            default:''
        }
    },
    mounted(){
        this.getChartData(),
        this.getCommandData()
    },
    data(){
        return{
            data:[],
            total:0,
            query:{
                current:1,
                size:6,
                username:this.userName
            },
            ChartData:{},
        }
    },
    methods:{
        getChartData(){
            getUserChart(7,this.userName).then(r=>{
                this.ChartData=r.object
                console.log(this.ChartData)
            }).catch()
        },
        getCommandData(){
            getHistoryCommand(this.query).then(r=>{
                this.data = r.object.records
                this.query.current=r.object.current
                this.query.size=r.object.size
                this.total=r.object.total
                console.log(r.object,'历史代码')
            }).catch(e=>{
                console.log(e)
            })
        }
    },
    watch:{
        'userName':function(newVal){
            this.getChartData();
        }
    },
    components:{
        ECharts
    }
}
</script>
<style scoped>

</style>