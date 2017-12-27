import { NgModule } from '@angular/core';
import { PreloadAllModules, RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  {
    path:         '',
    loadChildren: './dj/dj.module#DjModule'
  },
  {
    path:         'login',
    loadChildren: './login/login.module#LoginModule'
  }
];

@NgModule({
  imports:   [
    RouterModule.forRoot(routes, { preloadingStrategy: PreloadAllModules })
  ],
  providers: [],
  exports:   [
    RouterModule
  ]
})
export class AppRoutingModule {}
