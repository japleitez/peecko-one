import dayjs from 'dayjs/esm';
import { FieldAccess } from '../../shared/profile/view.models';

export interface ITrialLicense {
  license: string;
  expirationDate?: dayjs.Dayjs | null;
  notes?: string | null;
}

export type NewTrialLicense = Omit<ITrialLicense, 'license'> & { license: null };

export interface TrialLicenseAccess {
  license: FieldAccess;
  expirationDate: FieldAccess;
  notes: FieldAccess;
}

export let TRIAL_LICENSE_ACCESS: TrialLicenseAccess;

TRIAL_LICENSE_ACCESS = {
  license: { listable: true, visible: true, disabled: false },
  expirationDate: { listable: true, visible: true, disabled: false },
  notes: { listable: true, visible: true, disabled: false },
};
