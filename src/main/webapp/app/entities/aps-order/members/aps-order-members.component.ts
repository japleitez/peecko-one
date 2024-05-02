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
import { MatToolbarModule } from '@angular/material/toolbar';
import { ApsOrderService } from '../service/aps-order.service';

@Component({
  selector: 'jhi-aps-order-members',
  standalone: true,
  imports: [
    MatDialogContent,
    MatDialogActions,
    MatButtonModule,
    MatDialogTitle,
    MatInputModule,
    FormsModule,
    MatToolbarModule
  ],
  templateUrl: './aps-order-members.component.html',
  styleUrl: './aps-order-members.component.scss'
})
export class ApsOrderMembersComponent {
  currentFile?: File;
  fileName: string = 'Select File';

  constructor(
    protected apsOrderService: ApsOrderService,
    public dialogRef: MatDialogRef<ApsOrderMembersComponent>,
    @Inject(MAT_DIALOG_DATA) public data: IApsOrderInfo,
  ) { }

  selectFile(event: any) {
    if (event.target.files && event.target.files[0]) {
      const file: File = event.target.files[0];
      this.currentFile = file;
      this.fileName = this.currentFile.name;
    } else {
      this.fileName = 'Select File';
    }
  }

  upload(): void {
    if (this.currentFile) {
      const formData = new FormData();
      formData.append("apsOrderId", this.data.id.toString());
      formData.append('file', this.currentFile);
      this.apsOrderService.importMembers(formData).subscribe(
        (response) => {
          alert("File was uploaded " + response.status);
        });
    }
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

}
