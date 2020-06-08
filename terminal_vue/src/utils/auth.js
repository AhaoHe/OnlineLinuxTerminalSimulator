import { constantRoutes , asyncRoutes } from '@/router'
import router from '@/router'

const TokenKey = 'LinuxTerminal-Token'
const RoleKey = 'LinuxTerminal-Role'
const UsernameKey = 'LinuxTerminal-Username'
const LevelKey = 'LinuxTerminal-Level'
const GroupIdKey = 'LinuxTerminal-GroupId'
const GnameKey = 'LinuxTerminal-GName'
const RouterKey = 'LinuxTerminal-Router'

export function getToken() {
  return sessionStorage.getItem(TokenKey)
}

export function setToken(token) {
  return sessionStorage.setItem(TokenKey, token)
}

export function removeToken() {
  return sessionStorage.removeItem(TokenKey)
}

export function getRole() {
  return sessionStorage.getItem(RoleKey)
}

export function setRole(role) {
  return sessionStorage.setItem(RoleKey, role)
}

export function removeRole() {
  return sessionStorage.removeItem(RoleKey)
}

export function getUsername() {
  return sessionStorage.getItem(UsernameKey)
}

export function setUsername(username) {
  return sessionStorage.setItem(UsernameKey, username)
}

export function removeUsername() {
  return sessionStorage.removeItem(UsernameKey)
}

export function getLevel() {
  return sessionStorage.getItem(LevelKey)
}
export function setLevel(level) {
  return sessionStorage.setItem(LevelKey, level)
}
export function removeLevel() {
  return sessionStorage.removeItem(LevelKey)
}

/* 组id */
export function getGroupId() {
  return sessionStorage.getItem(GroupIdKey)
}
export function setGroupId(GroupId) {
  return sessionStorage.setItem(GroupIdKey, GroupId)
}
export function removeGroupId() {
  return sessionStorage.removeItem(GroupIdKey)
}

/* 组名 */
export function getGName() {
  return sessionStorage.getItem(GnameKey)
}
export function setGName(gname) {
  return sessionStorage.setItem(GnameKey, gname)
}
export function removeGName() {
  return sessionStorage.removeItem(GnameKey)
}

/* routers */
export function getRouter() {
  const routers=JSON.parse(sessionStorage.getItem(RouterKey))
  console.log(routers)
  if(routers!=null&&routers!=undefined){
    console.log(asyncRoutes)
    router.addRoutes(routers);
    router.addRoutes(asyncRoutes);
    next({ ...to, replace: true })
  }
  return routers
}
export function setRouter(routers) {
  return sessionStorage.setItem(RouterKey, JSON.stringify(routers))
}
export function removeRouter() {
  return sessionStorage.removeItem(RouterKey)
}