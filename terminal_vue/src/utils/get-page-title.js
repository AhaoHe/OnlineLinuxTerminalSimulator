import settings from '@/settings'

const title = settings.title || '在线Linux终端平台'

export default function getPageTitle(pageTitle) {
    if (pageTitle) {
      return `${pageTitle} - ${title}`
    }
    return `${title}`
}