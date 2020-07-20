import Layout from '@/layout/index'

export function generaMenu(routers,data){
    data.forEach((item) => {
        let menu = {
            name: item.menuName,
            path: item.url,
            component: !item.url ?  Layout : () => import(`@/views${item.component}`),
            meta:{
                title: item.menuName,
                icon: item.icon,
                id: item.id
            },
            children: [],
        }
        data.remove(item);
        for(var child in data){
            if(child.pid && child.pid == item.id){
                let menu_child = {
                    name: child.menuName,
                    path: child.url,
                    component: !child.url ?  Layout : () => import(`@/views${child.component}`),
                    meta:{
                        title: child.menuName,
                        icon: child.icon,
                        id: child.id
                    },
                }
                menu.children.push(menu_child);
            }else{
                routers.push(menu);
            }
        }
    });
}