import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import SharedModule from 'app/shared/shared.module';

import { User } from '../user-management.model';
import { AgencyService } from 'app/entities/agency/service/agency.service';
import { IAgency } from 'app/entities/agency/agency.model';

@Component({
  standalone: true,
  selector: 'jhi-user-mgmt-detail',
  templateUrl: './user-management-detail.component.html',
  imports: [SharedModule],
})
export default class UserManagementDetailComponent implements OnInit {
  user: User | null = null;
  agency: IAgency | null = null;

  constructor(
    private route: ActivatedRoute,
    private agencyService: AgencyService,
  ) {}

  ngOnInit(): void {
    this.route.data.subscribe(({ user }) => {
      this.user = user;
      if (user?.agencyId) {
        this.agencyService.find(user.agencyId).subscribe(res => (this.agency = res.body));
      }
    });
  }
}
