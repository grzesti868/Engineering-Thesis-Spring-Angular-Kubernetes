import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './auth/auth.guard';
import { LoginComponent } from './auth/login/login.component';
import { ProductComponent } from './products/product/product.component';
import { ProductsComponent } from './products/products/products.component';


const routes: Routes = [
 //{path: '', component: AddUserComponent },
 {path: 'logowanie', component: LoginComponent },
 {path: 'produkty', canActivate: [AuthGuard] , children: [
   { path: '', component: ProductsComponent, canActivate: [AuthGuard] },
   { path: ':id', component: ProductComponent, canActivate: [AuthGuard], data: { accessRule: "ROLE_ADMIN" } },
   { path: 'nowy', component: ProductComponent, canActivate: [AuthGuard] ,data: { accessRule: "ROLE_ADMIN"} },
 ] 
}
 

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
