const getters = {
    token: state => state.user.token,
    roles: state => state.user.roles,
    username: state => state.user.username,
    gname: state => state.user.gname,
    level: state => state.user.level,
    groupId: state => state.user.groupId,
    menu_list: state => state.user.menu_list,

    sidebar: state => state.app.sidebar,
    size: state => state.app.size,
    device: state => state.app.device,

    permission_routes: state => state.permission.routes,
}
export default getters
  