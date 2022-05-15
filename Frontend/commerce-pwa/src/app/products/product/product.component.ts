import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { Product } from '../product';
import { ProductsService } from '../products.service';

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css']
})
export class ProductComponent implements OnInit {

  form!: FormGroup;
  id: number = 0;
  orgName: string = '';
  err: string = '';

  constructor(private fb: FormBuilder, private route: ActivatedRoute, private productsService: ProductsService, private router: Router, private snackBar: MatSnackBar) { }

  ngOnInit(): void {
    this.id = Number.parseInt(this.route.snapshot.paramMap.get('id') || '0')

    if(this.id > 0) {
      this.productsService.getProduct(this.id).subscribe(p => this.createForm(p));
      
    } else {
      var false_prodcut;
      this.createForm(false_prodcut);
    }
  }

  private createForm(product?: Product) {
    this.orgName = product?.name || 'null';
    this.form = this.fb.group({
      name: new FormControl(product?.name, [Validators.required, Validators.minLength(2), Validators.maxLength(100)]),
      imgFile: new FormControl(product?.imgFile, [Validators.required]),
      quantity: new FormControl(product?.quantity,[Validators.required, Validators.pattern("^[0-9]*$"), Validators.min(0)] ),
      basePricePerUnit: new FormControl(product?.basePricePerUnit.basePricePerUnit, [Validators.required])
    });
  }

  onSubmit() {
    this.err='';
    if(this.id > 0) {
      console.log("Edycja produktu.")
      this.productsService.editProduct(this.orgName, this.form.value).subscribe(o => this.router.navigateByUrl('produkty'));
    } else {
      console.log("Dodanie produktu.")
      this.productsService.addProduct(this.form.value).subscribe((res) => {this.router.navigateByUrl('produkty')},
      (error) => {
        console.log(JSON.parse(error.error));
        this.err = JSON.parse(error.error).message;
          this.snackBar.open(this.err, "Rozumiem", {
          duration: 4000,
          panelClass: ['my-snack-bar-container']
        });
        
      }
      )
      if(this.err!='') {
        this.router.navigateByUrl('/produkty');
      }

      
      
    }
  }

}
