import { AbstractControl, FormControl, ValidationErrors } from '@angular/forms';

/**
 * Autocomplete controls hold the selected option object, but the user can also type free text, which leaves a plain string
 * in the control. A non-empty string means nothing was picked from the list, so the value must not be submitted.
 */
export function selectionRequiredValidator(control: AbstractControl): ValidationErrors | null {
  const value = control.value;
  return typeof value === 'string' && value.trim() !== '' ? { selectionRequired: true } : null;
}

/**
 * Resolves typed text left in an autocomplete control: empty text clears the control, and text that matches exactly one
 * option's label (ignoring case) selects that option. Any other text is left in place for selectionRequiredValidator to flag.
 */
export function resolveTypedSelection<T>(
  control: FormControl<T | string | null>,
  options: T[],
  label: (option: T) => string | null | undefined,
): void {
  const value = control.value;
  if (typeof value !== 'string') {
    return;
  }
  const typed = value.trim().toLowerCase();
  if (typed === '') {
    control.setValue(null);
    return;
  }
  const matches = options.filter(option => label(option)?.trim().toLowerCase() === typed);
  if (matches.length === 1) {
    control.setValue(matches[0]);
  }
}
