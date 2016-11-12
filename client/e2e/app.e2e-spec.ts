import { PetProjectPage } from './app.po';

describe('pet-project App', function() {
  let page: PetProjectPage;

  beforeEach(() => {
    page = new PetProjectPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
