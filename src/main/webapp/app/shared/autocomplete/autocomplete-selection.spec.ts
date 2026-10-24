import { FormControl } from '@angular/forms';

import { resolveTypedSelection, selectionRequiredValidator } from './autocomplete-selection';

interface Option {
  id: number;
  name?: string | null;
}

describe('autocomplete selection', () => {
  const yoga: Option = { id: 1, name: 'Yoga' };
  const pilates: Option = { id: 2, name: 'Pilates' };
  const options = [yoga, pilates, { id: 3, name: null }];

  describe('selectionRequiredValidator', () => {
    it('should flag typed text that was not picked from the list', () => {
      expect(selectionRequiredValidator(new FormControl('Yog'))).toEqual({ selectionRequired: true });
    });

    it('should accept a selected option, null and empty text', () => {
      expect(selectionRequiredValidator(new FormControl<Option | null>(yoga))).toBeNull();
      expect(selectionRequiredValidator(new FormControl(null))).toBeNull();
      expect(selectionRequiredValidator(new FormControl('  '))).toBeNull();
    });
  });

  describe('resolveTypedSelection', () => {
    const create = (value: Option | string | null): FormControl<Option | string | null> =>
      new FormControl<Option | string | null>(value, { validators: [selectionRequiredValidator] });

    it('should select the option whose label matches the typed text, ignoring case and spaces', () => {
      const control = create(' yoga ');
      resolveTypedSelection(control, options, o => o.name);
      expect(control.value).toBe(yoga);
      expect(control.valid).toBe(true);
    });

    it('should clear the control when the text is empty', () => {
      const control = create('   ');
      resolveTypedSelection(control, options, o => o.name);
      expect(control.value).toBeNull();
    });

    it('should leave unmatched text in place so the control stays invalid', () => {
      const control = create('Yog');
      resolveTypedSelection(control, options, o => o.name);
      expect(control.value).toBe('Yog');
      expect(control.hasError('selectionRequired')).toBe(true);
    });

    it('should not pick an option when the text matches several options', () => {
      const control = create('yoga');
      resolveTypedSelection(control, [yoga, { id: 4, name: 'YOGA' }], o => o.name);
      expect(control.value).toBe('yoga');
    });

    it('should leave a selected option untouched', () => {
      const control = create(pilates);
      resolveTypedSelection(control, options, o => o.name);
      expect(control.value).toBe(pilates);
    });
  });
});
