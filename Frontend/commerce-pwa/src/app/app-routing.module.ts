import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login.component';
import { ProductComponent } from './products/product/product.component';
import { ProductsComponent } from './products/products/products.component';


const routes: Routes = [
 //{path: '', component: AddUserComponent },
 {path: 'logowanie', component: LoginComponent },
 {path: 'produkty', children: [
   { path: '', component: ProductsComponent },
   { path: ':id', component: ProductComponent },
 ] 
}
 

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
