import { Component, Inject } from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle
} from '@angular/material/dialog';
import { IApsOrderInfo } from '../aps-order.model';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'jhi-aps-order-members',
  standalone: true,
  imports: [
    MatDialogContent,
    MatDialogActions,
    MatButtonModule,
    MatDialogTitle,
    MatInputModule,
    FormsModule
  ],
  templateUrl: './aps-order-members.component.html',
  styleUrl: './aps-order-members.component.scss'
})
export class ApsOrderMembersComponent {

  constructor(
    public dialogRef: MatDialogRef<ApsOrderMembersComponent>,
    @Inject(MAT_DIALOG_DATA) public data: IApsOrderInfo,
  ) { }

  onNoClick(): void {
    this.dialogRef.close();
  }

}
