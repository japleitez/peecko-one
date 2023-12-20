import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'aps-user',
    data: { pageTitle: 'ApsUsers' },
    loadChildren: () => import('./aps-user/aps-user.routes'),
  },
  {
    path: 'aps-device',
    data: { pageTitle: 'ApsDevices' },
    loadChildren: () => import('./aps-device/aps-device.routes'),
  },
  {
    path: 'agency',
    data: { pageTitle: 'Agencies' },
    loadChildren: () => import('./agency/agency.routes'),
  },
  {
    path: 'staff',
    data: { pageTitle: 'Staff' },
    loadChildren: () => import('./staff/staff.routes'),
  },
  {
    path: 'customer',
    data: { pageTitle: 'Customers' },
    loadChildren: () => import('./customer/customer.routes'),
  },
  {
    path: 'contact',
    data: { pageTitle: 'Contacts' },
    loadChildren: () => import('./contact/contact.routes'),
  },
  {
    path: 'aps-plan',
    data: { pageTitle: 'ApsPlans' },
    loadChildren: () => import('./aps-plan/aps-plan.routes'),
  },
  {
    path: 'aps-order',
    data: { pageTitle: 'ApsOrders' },
    loadChildren: () => import('./aps-order/aps-order.routes'),
  },
  {
    path: 'aps-membership',
    data: { pageTitle: 'ApsMemberships' },
    loadChildren: () => import('./aps-membership/aps-membership.routes'),
  },
  {
    path: 'aps-pricing',
    data: { pageTitle: 'ApsPricings' },
    loadChildren: () => import('./aps-pricing/aps-pricing.routes'),
  },
  {
    path: 'video',
    data: { pageTitle: 'Videos' },
    loadChildren: () => import('./video/video.routes'),
  },
  {
    path: 'video-category',
    data: { pageTitle: 'VideoCategories' },
    loadChildren: () => import('./video-category/video-category.routes'),
  },
  {
    path: 'article',
    data: { pageTitle: 'Articles' },
    loadChildren: () => import('./article/article.routes'),
  },
  {
    path: 'article-series',
    data: { pageTitle: 'ArticleSeries' },
    loadChildren: () => import('./article-series/article-series.routes'),
  },
  {
    path: 'article-category',
    data: { pageTitle: 'ArticleCategories' },
    loadChildren: () => import('./article-category/article-category.routes'),
  },
  {
    path: 'notification',
    data: { pageTitle: 'Notifications' },
    loadChildren: () => import('./notification/notification.routes'),
  },
  {
    path: 'label-translation',
    data: { pageTitle: 'LabelTranslations' },
    loadChildren: () => import('./label-translation/label-translation.routes'),
  },
  {
    path: 'play-list',
    data: { pageTitle: 'PlayLists' },
    loadChildren: () => import('./play-list/play-list.routes'),
  },
  {
    path: 'video-item',
    data: { pageTitle: 'VideoItems' },
    loadChildren: () => import('./video-item/video-item.routes'),
  },
  {
    path: 'coach',
    data: { pageTitle: 'Coaches' },
    loadChildren: () => import('./coach/coach.routes'),
  },
  {
    path: 'invoice',
    data: { pageTitle: 'Invoices' },
    loadChildren: () => import('./invoice/invoice.routes'),
  },
  {
    path: 'invoice-item',
    data: { pageTitle: 'InvoiceItems' },
    loadChildren: () => import('./invoice-item/invoice-item.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
