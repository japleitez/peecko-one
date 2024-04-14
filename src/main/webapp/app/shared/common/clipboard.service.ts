import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})

export class ClipboardService {

  constructor() { }

  public copy(selector: any) {
    let element = document.querySelector(selector);
    let textToCopy = '';

    // Check if it's an input field
    if (['INPUT', 'TEXTAREA'].includes(element.tagName)) {
      element.select();
      textToCopy = element.value;
    } else {
      textToCopy = element.innerText;
      // For non-input elements
      const textArea = document.createElement('textarea');
      textArea.value = textToCopy;
      document.body.appendChild(textArea);
      textArea.select();
      document.body.removeChild(textArea);
    }

    navigator.clipboard.writeText(textToCopy)
      .then(() => console.log('Text copied! ' + textToCopy))
      .catch(err => console.error('Error copying text: ', err));

  }

}
